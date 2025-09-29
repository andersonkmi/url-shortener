package org.codecraftlabs.shorturl.service;

import org.codecraftlabs.shorturl.service.repository.DatabaseException;
import org.codecraftlabs.shorturl.service.repository.ShortenedURL;
import org.codecraftlabs.shorturl.service.repository.URLShortnerCachingRepository;
import org.codecraftlabs.shorturl.service.repository.URLShortnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(urlShortnerRepository.findShortenedUrl("http://www.test.com"))
                .thenReturn(of(new ShortenedURL(1L, "http://www.test.com", "123")));

        // Runs the service
        String shortUrl = urlShortnerService.generateShortUrl("http://www.test.com");

        // Runs assertions
        assertThat(shortUrl).isEqualTo("123");
        verify(urlShortnerRepository, never()).getUrlId();
        verify(base62Converter, never()).toBase62(anyLong());
        verify(urlShortnerRepository, never()).saveShortUrl(anyLong(), anyString(), anyString());
        verify(urlShortnerCachingRepository, never()).setValue(anyString(), anyString());
    }

    @Test
    public void when_generating_short_url_db_doesnt_have_it_already_do_from_scratch() throws DatabaseException {
        // Setup mocks
        when(urlShortnerRepository.findShortenedUrl("http://www.test.com")).thenReturn(empty());
        when(urlShortnerRepository.getUrlId()).thenReturn(13292929L);
        when(base62Converter.toBase62(13292929L)).thenCallRealMethod();

        // Runs the service and assertions
        String generatedShortUrl = urlShortnerService.generateShortUrl("http://www.test.com");
        assertThat(generatedShortUrl).isEqualTo("TM65");

        // Verifies other calls
        ArgumentCaptor<String> shortUrlRepositoryArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> originalUrlRepositoryArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> urlIdRepositoryArgument = ArgumentCaptor.forClass(Long.class);
        verify(urlShortnerRepository, times(1))
                .saveShortUrl(urlIdRepositoryArgument.capture(), originalUrlRepositoryArgument.capture(), shortUrlRepositoryArgument.capture());
        assertThat(urlIdRepositoryArgument.getValue()).isEqualTo(13292929L);
        assertThat(originalUrlRepositoryArgument.getValue()).isEqualTo("http://www.test.com");
        assertThat(shortUrlRepositoryArgument.getValue()).isEqualTo("TM65");

        ArgumentCaptor<String> shortUrlCacheRepositoryArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> originalUrlCacheRepositoryArgument = ArgumentCaptor.forClass(String.class);
        verify(urlShortnerCachingRepository, times(1))
                .setValue(shortUrlCacheRepositoryArgument.capture(), originalUrlCacheRepositoryArgument.capture());
        assertThat(originalUrlCacheRepositoryArgument.getValue()).isEqualTo("http://www.test.com");
        assertThat(shortUrlCacheRepositoryArgument.getValue()).isEqualTo("TM65");
    }

    @Test
    public void when_db_fails_is_raised_should_throw_exception() throws DatabaseException {
        // Setup mocks
        when(urlShortnerRepository.findShortenedUrl("http://www.test.com")).thenReturn(empty());
        when(urlShortnerRepository.getUrlId()).thenThrow(new DatabaseException("Failed to call database"));

        assertThatExceptionOfType(URLShorteningException.class)
                .isThrownBy(() -> urlShortnerService.generateShortUrl("http://www.test.com"))
                .withMessageContaining("Fail to generate short URL");

        verify(base62Converter, never()).toBase62(anyLong());
        verify(urlShortnerRepository, never()).saveShortUrl(anyLong(), anyString(), anyString());
        verify(urlShortnerCachingRepository, never()).setValue(anyString(), anyString());
    }
}
