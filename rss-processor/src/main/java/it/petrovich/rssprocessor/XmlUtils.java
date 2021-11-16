package it.petrovich.rssprocessor;

import it.petrovich.rss.xml.atom.DateTimeType;
import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import jakarta.xml.bind.JAXBElement;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.Optional;

@Slf4j
@UtilityClass
public class XmlUtils {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z");
    public static final String DATE_TIME_ELEM_CLASS = DateTimeType.class.getSimpleName();
    public static final String JAXB_ELEM_CLASS = JAXBElement.class.getSimpleName();
    public static final String STRING_CLASS = "String";
    public static final String CALENDAR_CLASS = "DateTimeType";

    public static Optional<Object> extractEntry(TRss feed, String fieldName) {
        return feed
                .getChannel()
                .getTitleOrLinkOrDescription()
                .stream()
                .filter(item -> item.getClass().getSimpleName().equalsIgnoreCase(JAXB_ELEM_CLASS))
                .map(JAXBElement.class::cast)
                .filter(item -> item.getName().getLocalPart().equals(fieldName))
                .findFirst()
                .map(JAXBElement::getValue);
    }

    public static LocalDateTime parseDate(Object source) {
        return switch (source.getClass().getSimpleName()) {
            case STRING_CLASS -> stringToDate((String) source);
            case CALENDAR_CLASS -> calendarToDate((DateTimeType) source);
            default -> throw new RuntimeException("Unknown class type ");
        };
    }

    public static LocalDateTime stringToDate(String source) {
        return Optional
                .ofNullable(source)
                .map(str -> LocalDateTime.parse(str, FORMATTER))
                .orElse(LocalDateTime.now());
    }

    private static LocalDateTime calendarToDate(DateTimeType source) {
        return Optional
                .ofNullable(source)
                .map(DateTimeType::getValue)
                .map(XMLGregorianCalendar::toGregorianCalendar)
                .map(GregorianCalendar::toZonedDateTime)
                .map(ZonedDateTime::toLocalDateTime)
                .orElse(LocalDateTime.now());
    }

    public static Optional<Object> extractEntry(FeedType feed, String className) {
        return feed
                .getAuthorOrCategoryOrContributor()
                .stream()
                .map(JAXBElement.class::cast)
                .filter(elem -> elem.getDeclaredType().getSimpleName().equalsIgnoreCase(className))
                .findFirst()
                .map(JAXBElement::getValue);
    }
}
