package org.codecraftlabs.shorturl.service;

import javax.annotation.Nonnull;

public class Base62Converter {
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;


    @Nonnull
    public String toBase62(long number) {
        if (number == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();
        while (number > 0) {
            int remainder = (int) (number % BASE);
            result.append(BASE62_CHARS.charAt(remainder));
            number = number / BASE;
        }
        return result.reverse().toString();
    }

}
