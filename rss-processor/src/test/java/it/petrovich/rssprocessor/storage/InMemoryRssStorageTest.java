package it.petrovich.rssprocessor.storage;

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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {InMemoryRssStorage.class})
class InMemoryRssStorageTest {

    @Autowired
    private InMemoryRssStorage inMemoryRssStorage;

    @MockBean
    private RssXmlService rssXmlService;
    @MockBean
    private RequestService requestService;

    @Test
    void testValidation() {
        val saved = inMemoryRssStorage.put(buildRequest());
        assertNotNull(saved);
        assertTrue(saved.isPresent());
    }

    private StoreFeedRequest buildRequest() {
        return new StoreFeedRequest("TEST_NAME", "http://test.not/", 1, RssType.ATOM);
    }
}