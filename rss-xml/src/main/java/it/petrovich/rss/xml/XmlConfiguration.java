package it.petrovich.rss.xml;

import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XmlConfiguration {
    /**
     * Creates {@link JAXBContext} conyains all RSS objects.
     * At the moment supports only RSS 2.0 and Atom.
     *
     * @return {@link JAXBContext}
     */
    @Bean
    @SneakyThrows(value = {JAXBException.class})
    public JAXBContext jaxbContext() {
        return JAXBContext.newInstance(TRss.class, FeedType.class);
    }
}
