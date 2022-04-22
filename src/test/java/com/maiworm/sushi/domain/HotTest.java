package com.maiworm.sushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maiworm.sushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hot.class);
        Hot hot1 = new Hot();
        hot1.setId(1L);
        Hot hot2 = new Hot();
        hot2.setId(hot1.getId());
        assertThat(hot1).isEqualTo(hot2);
        hot2.setId(2L);
        assertThat(hot1).isNotEqualTo(hot2);
        hot1.setId(null);
        assertThat(hot1).isNotEqualTo(hot2);
    }
}
