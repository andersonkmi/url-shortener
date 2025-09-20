package org.codecraftlabs.shorturl.api;

import org.codecraftlabs.shorturl.service.URLShorteningException;
import org.codecraftlabs.shorturl.service.URLShortnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/url-shortner/v1")
public class UrlShortnerRestController {
    private final URLShortnerService urlShortnerService;

    @Autowired
    public UrlShortnerRestController(@Nonnull URLShortnerService urlShortnerService) {
        this.urlShortnerService = urlShortnerService;
    }

    @PostMapping(value = "/url", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<URLResponse> createShortenedUrl(@RequestBody URLRequest urlRequest) {
        try {
            String shortUrl = urlShortnerService.generateShortUrl(urlRequest.getUrl());
            URLResponse response = new URLResponse();
            response.setUrl(urlRequest.getUrl());
            response.setShortUrl(shortUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (URLShorteningException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL not shortened", exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal system error", exception);
        }
    }

    @GetMapping(value = "/url/{shortenedUrl}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUrl(@PathVariable String shortenedUrl) {
        return ResponseEntity.ok("test");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
