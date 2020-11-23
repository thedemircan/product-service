package com.swapping.productservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WordUtil {

    private static final char[] PUNCTUATION = {'.', ' ', '!', '?', ';'};

    public static String toCapitalizeFully(String string) {
        return WordUtils.capitalizeFully(string, PUNCTUATION);
    }
}
