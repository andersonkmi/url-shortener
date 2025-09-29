package org.codecraftlabs.shorturl.api;

import org.assertj.core.api.Assertions;
import org.codecraftlabs.shorturl.service.URLShortnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;

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
    public void when_url_found_should_return_200() {
        // Setup mocks
        Mockito.when(urlShortnerService.getOriginalUrl("TM65")).thenReturn(Optional.of("http://www.test.com"));

        ResponseEntity<String> response = urlShortnerRestController.getUrl("TM65");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        Assertions.assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("http://www.test.com"));
    }
}
