package it.petrovich.rssprocessor.storage;

import it.petrovich.rss.domain.Rss;
import it.petrovich.rss.domain.storing.RssType;
import it.petrovich.rss.domain.storing.StoreFeedRequest;
import it.petrovich.rss.storage.RssStorage;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static it.petrovich.rssprocessor.TestUtils.ann;
import static it.petrovich.rssprocessor.TestUtils.at;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {InMemoryRssStorage.class})
class InMemoryRssStorageTest {

    @Autowired
    private RssStorage storage;

    @Test
    void testValidation() {
        // when
        val saved = storage.put(Optional.of(buildRequest()));

        // then
        assertAll(
                ann(saved),
                at(saved.isPresent())
        );
    }

    private Rss buildRequest() {
        val request = new StoreFeedRequest("TEST_NAME", "http://test.not/", 1, RssType.ATOM);
        return Rss.fromRequest(request);
    }


}