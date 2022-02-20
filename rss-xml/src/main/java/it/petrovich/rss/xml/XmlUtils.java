package it.petrovich.rss.xml;

import it.petrovich.rss.xml.atom.DateTimeType;
import it.petrovich.rss.xml.atom.FeedType;
import it.petrovich.rss.xml.rss20111.TRss;
import it.petrovich.rss.xml.rss20111.TRssItem;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.JAXBElement;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;

import javax.xml.datatype.XMLGregorianCalendar;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.regex.Pattern;

@UtilityClass
public class XmlUtils {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z");
    public static final String DATE_TIME_ELEM_CLASS = DateTimeType.class.getSimpleName();
    public static final String JAXB_ELEM_CLASS = JAXBElement.class.getSimpleName();
    public static final String STRING_CLASS = "String";
    public static final String CALENDAR_CLASS = "DateTimeType";
    public static final String HTML_TAG_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
    public static final Pattern pattern = Pattern.compile(HTML_TAG_PATTERN);

    public static Optional<Object> extractEntry(final TRss feed, final String fieldName) {
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

    public static Object extractOrElse(final TRssItem tRssItem,
                                       @NotNull final String fieldName,
                                       @NotNull final Object orElse) {
        return extractEntry(tRssItem, fieldName)
                .orElse(orElse);
    }

    public static Optional<Object> extractEntry(final TRssItem tRssItem, @NotNull final String fieldName) {
        return tRssItem
                .getTitleOrDescriptionOrLink()
                .stream()
                .filter(item -> item.getClass().getSimpleName().equalsIgnoreCase(JAXB_ELEM_CLASS))
                .map(JAXBElement.class::cast)
                .filter(item -> item.getName().getLocalPart().equals(fieldName))
                .findFirst()
                .map(JAXBElement::getValue);
    }

    public static String castToString(Object source) {
        return source.getClass().isAssignableFrom(String.class) ? (String) source : "";
    }

    public static LocalDateTime parseDate(final Object source) {
        return switch (source.getClass().getSimpleName()) {
            case STRING_CLASS -> stringToDate((String) source);
            case CALENDAR_CLASS -> calendarToDate((DateTimeType) source);
            default -> throw new RuntimeException("Unknown class type ");
        };
    }

    public static LocalDateTime stringToDate(final String source) {
        return Optional
                .ofNullable(source)
                .map(str -> LocalDateTime.parse(str, FORMATTER))
                .orElse(LocalDateTime.now());
    }

    private static LocalDateTime calendarToDate(final DateTimeType source) {
        return Optional
                .ofNullable(source)
                .map(DateTimeType::getValue)
                .map(XMLGregorianCalendar::toGregorianCalendar)
                .map(GregorianCalendar::toZonedDateTime)
                .map(ZonedDateTime::toLocalDateTime)
                .orElse(LocalDateTime.now());
    }

    public static Optional<Object> extractEntry(final FeedType feed, @NotNull final String className) {
        return feed
                .getAuthorOrCategoryOrContributor()
                .stream()
                .map(JAXBElement.class::cast)
                .filter(elem -> className.equalsIgnoreCase(elem.getDeclaredType().getSimpleName()))
                .findFirst()
                .map(JAXBElement::getValue);
    }

    @SneakyThrows
    public static URL parse(final String source) {
        return new URL(source);
    }

    public static String extractParagraphs(final String sourceText) {
        if (!isHtml(sourceText)) {
            return sourceText;
        }
        return Jsoup.parse(sourceText).select("p").text();
    }

    private static boolean isHtml(String sourceText) {
        return pattern.matcher(sourceText).find();
    }
}
