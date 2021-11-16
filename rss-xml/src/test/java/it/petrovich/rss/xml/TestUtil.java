package it.petrovich.rss.xml;

import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.Objects;

@UtilityClass
public class TestUtil {

    @SneakyThrows(value = {JAXBException.class})
    public static JAXBContext initJaxbContext() {
        return JAXBContext.newInstance(TRss.class, FeedType.class);
    }

    @SneakyThrows(value = {JAXBException.class})
    public static Unmarshaller initUnmarshaller(JAXBContext ctx) {
        return ctx.createUnmarshaller();
    }

    public static StreamSource buildSource(InputStream xmlStream) {
        return new StreamSource(xmlStream);
    }

    @SneakyThrows
    public static InputStream readXml(String path) {
        return Objects.requireNonNull(TestUtil.class.getResourceAsStream(path));
    }
}
