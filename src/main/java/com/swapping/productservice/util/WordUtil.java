package com.swapping.productservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.text.WordUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WordUtil {

    public static String toTitle(String string) {
        return WordUtils.capitalizeFully(string);
    }
}
