package com.maiworm.sushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maiworm.sushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SushiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sushi.class);
        Sushi sushi1 = new Sushi();
        sushi1.setId(1L);
        Sushi sushi2 = new Sushi();
        sushi2.setId(sushi1.getId());
        assertThat(sushi1).isEqualTo(sushi2);
        sushi2.setId(2L);
        assertThat(sushi1).isNotEqualTo(sushi2);
        sushi1.setId(null);
        assertThat(sushi1).isNotEqualTo(sushi2);
    }
}
