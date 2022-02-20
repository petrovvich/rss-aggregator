package it.petrovich.rssprocessor.service;

import com.google.common.io.Resources;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncHttpRequestService implements RequestService {

    @Override
    @SneakyThrows(value = {IOException.class})
    public String getRss(final URL url) {
        return Resources.toString(url, StandardCharsets.UTF_8);
    }
}
