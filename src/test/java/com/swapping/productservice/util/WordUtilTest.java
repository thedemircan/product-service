package com.swapping.productservice.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WordUtilTest {

    @Test
    public void it_should_to_title() {
        // When
        String title = WordUtil.toTitle("mehmet demircan");

        // Then
        assertThat(title).isEqualTo("Mehmet Demircan");
    }
}