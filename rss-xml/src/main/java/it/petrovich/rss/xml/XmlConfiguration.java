package it.petrovich.rss.xml;

import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.SneakyThrows;

public class XmlConfiguration {

    /**
     * This field exists only for caching purposes.
     * Many times created JAXBContext class has potentially memory leak issues.
     */
    private static final JAXBContext JAXB_CONTEXT = initContext();

    @SneakyThrows(value = {JAXBException.class})
    private static JAXBContext initContext() {
        return JAXBContext.newInstance(TRss.class, FeedType.class);
    }

    public static JAXBContext getJaxbCtx() {
        return JAXB_CONTEXT;
    }
}
