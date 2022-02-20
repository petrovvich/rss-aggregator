package it.petrovich.rssprocessor.service;

import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import it.petrovich.rss.xml.XmlUtils;
import jakarta.xml.bind.JAXBContext;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static it.petrovich.rss.xml.XmlUtils.extractEntry;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SyncHttpRequestService.class})
class RequestServiceTest {

    @Autowired
    private RequestService requestService;

    private final JAXBContext jaxbContext = buildJaxbContext();


    @Test
    @SneakyThrows
    void testXmlSerialization() {
        val unmarshaller = jaxbContext.createUnmarshaller();
        val rssContent = requestService.getRss(new URL("https://www.infoworld.com/category/java/index.rss"));

        val rssObject = unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(rssContent.getBytes(StandardCharsets.UTF_8))), TRss.class).getValue();
        val lastBuildDate = extractEntry(rssObject, "lastBuildDate")
                .map(XmlUtils::parseDate)
                .orElse(LocalDateTime.now());
        val entries = rssObject.getChannel().getItem();

        assertNotNull(rssObject);
        assertNotNull(lastBuildDate);
        assertNotNull(entries);
    }


    @SneakyThrows
    private JAXBContext buildJaxbContext() {
        return JAXBContext.newInstance(TRss.class, FeedType.class);
    }

}