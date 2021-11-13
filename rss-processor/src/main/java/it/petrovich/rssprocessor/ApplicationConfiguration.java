package it.petrovich.rssprocessor;

import it.petrovich.rssprocessor.xml.atom.FeedType;
import it.petrovich.rssprocessor.xml.rss20111.TRss;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.SneakyThrows;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @SneakyThrows(value = {JAXBException.class})
    public JAXBContext jaxbContext() {
        return JAXBContext.newInstance(TRss.class, FeedType.class);
    }
}
