package it.petrovich.rssprocessor.storage;

import it.petrovich.rss.common.FeedEntry;
import it.petrovich.rss.common.Pair;
import it.petrovich.rss.common.RssType;
import it.petrovich.rss.common.StoreFeedRequest;
import it.petrovich.rss.requester.RequestService;
import it.petrovich.rss.storage.RssStorage;
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

import static it.petrovich.rssprocessor.TestUtils.ae;
import static it.petrovich.rssprocessor.TestUtils.ann;
import static it.petrovich.rssprocessor.TestUtils.at;
import static org.junit.jupiter.api.Assertions.assertAll;

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
                ann(saved),
                at(saved.isPresent())
        );
    }

    @Test
    void testPutEntry_shouldReturnTrue() {
        // given
        val expectedUuid = UUID.randomUUID();
        val expectedEntries = buildEntries(1);

        // when
        boolean actual = storage.putOrReplaceEntry(new Pair<>(expectedUuid, expectedEntries));

        // then
        assertAll(
                at(actual),
                ae(expectedEntries, storage.getEntries(expectedUuid))
        );
    }

    @Test
    void testContainsEntry_shouldReturnTrue() {
        // given
        val expectedUuid = UUID.randomUUID();
        val expectedEntries = buildEntries(1);
        storage.putOrReplaceEntry(new Pair<>(expectedUuid, expectedEntries));

        // when
        boolean actual = storage.containsEntry(new Pair<>(expectedUuid, expectedEntries.stream().findAny().get()));

        // then
        assertAll(
                at(actual)
        );
    }

    @Test
    void testPutOrReplace_shouldReturn2elements() {
        // given
        val expectedUuid = UUID.randomUUID();
        val expectedEntries = buildEntries(1);
        boolean expectedFirst = storage.putOrReplaceEntry(new Pair<>(expectedUuid, expectedEntries));

        assertAll(
                at(expectedFirst),
                ae(1, storage.getEntries(expectedUuid).size()),
                ae(expectedEntries, storage.getEntries(expectedUuid))
        );

        val expectedEntriesSecond = new Pair<>(expectedUuid, buildEntries(4));

        boolean expectedSecond = storage.putOrReplaceEntry(expectedEntriesSecond);

        // then
        assertAll(
                at(expectedSecond),
                ae(4, storage.getEntries(expectedUuid).size()),
                ae(expectedEntriesSecond.right(), storage.getEntries(expectedUuid))
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