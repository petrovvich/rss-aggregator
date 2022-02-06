package it.petrovich.rssprocessor.storage;

import it.petrovich.rssprocessor.dto.FeedEntry;
import it.petrovich.rssprocessor.dto.Pair;
import it.petrovich.rssprocessor.dto.RssType;
import it.petrovich.rssprocessor.dto.StoreFeedRequest;
import it.petrovich.rssprocessor.service.RequestService;
import it.petrovich.rssprocessor.service.RssXmlService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {InMemoryRssStorage.class})
class InMemoryRssStorageTest {

    @Autowired
    private RssStorage storage;

    @MockBean
    private RssXmlService rssXmlService;
    @MockBean
    private RequestService requestService;

    @Test
    void testValidation() {
        // when
        val saved = storage.putRequest(buildRequest());

        // then
        assertAll(
                () -> assertNotNull(saved),
                () -> assertTrue(saved.isPresent())
        );
    }

    @Test
    void testPutEntry_shouldReturnTrue() {
        // given
        val expectedUuid = UUID.randomUUID();
        val expectedEntries = buildEntries(1);

        // when
        boolean actual = storage.putEntry(new Pair<>(expectedUuid, expectedEntries));

        // then
        assertAll(
                () -> assertTrue(actual),
                () -> assertEquals(expectedEntries, storage.getEntries(expectedUuid))
        );
    }

    private Collection<FeedEntry> buildEntries(int count) {
        return IntStream
                .range(0, count)
                .mapToObj(num -> new FeedEntry(num, true))
                .toList();
    }

    private StoreFeedRequest buildRequest() {
        return new StoreFeedRequest("TEST_NAME", "http://test.not/", 1, RssType.ATOM);
    }


}