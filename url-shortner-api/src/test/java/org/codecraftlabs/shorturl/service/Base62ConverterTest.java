package org.codecraftlabs.shorturl.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Base62ConverterTest {
    private Base62Converter base62Converter;

    @BeforeEach
    public void setup() {
        this.base62Converter = new Base62Converter();
    }

    @Test
    public void testBase62Conversion() {
        String convertedValue = this.base62Converter.toBase62(1000L);
        Assertions.assertThat(convertedValue).isEqualTo("g8");
    }

    @Test
    public void testBase62ConversionZero() {
        String convertedValue = this.base62Converter.toBase62(0);
        Assertions.assertThat(convertedValue).isEqualTo("0");
    }
}
