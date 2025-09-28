package org.codecraftlabs.shorturl.service;

import org.assertj.core.api.Assertions;
import org.codecraftlabs.shorturl.service.repository.DatabaseException;
import org.codecraftlabs.shorturl.service.repository.ShortenedURL;
import org.codecraftlabs.shorturl.service.repository.URLShortnerCachingRepository;
import org.codecraftlabs.shorturl.service.repository.URLShortnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class URLShortnerServiceTest {
    @Mock
    private URLShortnerRepository urlShortnerRepository;
    @Mock
    private URLShortnerCachingRepository urlShortnerCachingRepository;
    @Mock
    private Base62Converter base62Converter;

    @InjectMocks
    private URLShortnerService urlShortnerService;

    @Test
    public void when_generating_short_url_database_already_has_from_db() throws DatabaseException {
        // Setup mocks
        Mockito.when(urlShortnerRepository.findShortenedUrl("http://www.test.com"))
                .thenReturn(Optional.of(new ShortenedURL(1L, "http://www.test.com", "123")));

        // Runs the service
        String shortUrl = urlShortnerService.generateShortUrl("http://www.test.com");

        Assertions.assertThat(shortUrl).isEqualTo("123");
        Mockito.verify(urlShortnerRepository, Mockito.never()).getUrlId();
        Mockito.verify(base62Converter, Mockito.never()).toBase62(anyLong());
        Mockito.verify(urlShortnerRepository, Mockito.never()).saveShortUrl(anyLong(), anyString(), anyString());
        Mockito.verify(urlShortnerCachingRepository, Mockito.never()).setValue(anyString(), anyString());
    }
}
