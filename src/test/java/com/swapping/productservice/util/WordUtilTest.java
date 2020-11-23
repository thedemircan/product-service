package com.swapping.productservice.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WordUtilTest {

    @Test
    public void it_should_to_title() {
        // When
        String title = WordUtil.toCapitalizeFully("mehmet.nuri demircan");

        // Then
        assertThat(title).isEqualTo("Mehmet.Nuri Demircan");
    }
}