package org.codecraftlabs.shorturl.api;

import org.assertj.core.api.Assertions;
import org.codecraftlabs.shorturl.service.URLShorteningException;
import org.codecraftlabs.shorturl.service.URLShortnerService;
import org.codecraftlabs.shorturl.service.repository.DatabaseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(MockitoExtension.class)
public class UrlShortnerRestControllerTest {
    @Mock
    private URLShortnerService urlShortnerService;

    @InjectMocks
    private UrlShortnerRestController urlShortnerRestController;

    @Test
    public void when_url_not_found_should_return_404() {
        // Setup mocks
        Mockito.when(urlShortnerService.getOriginalUrl("TM65")).thenReturn(Optional.empty());

        ResponseEntity<String> response = urlShortnerRestController.getUrl("TM65");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void when_url_found_should_return_302() {
        // Setup mocks
        Mockito.when(urlShortnerService.getOriginalUrl("TM65")).thenReturn(Optional.of("http://www.test.com"));

        ResponseEntity<String> response = urlShortnerRestController.getUrl("TM65");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        Assertions.assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("http://www.test.com"));
    }

    @Test
    public void when_short_url_generated_should_return_201() {
        // Setup mock
        Mockito.when(urlShortnerService.generateShortUrl("http://www.test.com")).thenReturn("TM65");

        URLRequest request = new URLRequest();
        request.setUrl("http://www.test.com");
        var response = urlShortnerRestController.createShortenedUrl(request);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var body = response.getBody();
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getUrl()).isEqualTo("http://www.test.com");
        Assertions.assertThat(body.getShortUrl()).isEqualTo("TM65");
    }

    @Test
    public void when_url_shortening_exception_should_return_400() {
        // Setup mock
        Mockito.when(urlShortnerService.generateShortUrl("http://www.test.com"))
                .thenThrow(new URLShorteningException("Failed to process", new DatabaseException("Failed to access database")));

        URLRequest request = new URLRequest();
        request.setUrl("http://www.test.com");
        var exception = catchThrowable(() -> urlShortnerRestController.createShortenedUrl(request));
        Assertions.assertThat(exception).isInstanceOf(ResponseStatusException.class);

        ResponseStatusException originalException = (ResponseStatusException) exception;
        Assertions.assertThat(originalException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void when_other_exception_should_return_500() {
        // Setup mock
        Mockito.when(urlShortnerService.generateShortUrl("http://www.test.com"))
                .thenThrow(new NullPointerException("Test null pointer"));

        URLRequest request = new URLRequest();
        request.setUrl("http://www.test.com");
        var exception = catchThrowable(() -> urlShortnerRestController.createShortenedUrl(request));
        Assertions.assertThat(exception).isInstanceOf(ResponseStatusException.class);

        ResponseStatusException originalException = (ResponseStatusException) exception;
        Assertions.assertThat(originalException.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
