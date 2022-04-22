package com.maiworm.sushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maiworm.sushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SashimiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sashimi.class);
        Sashimi sashimi1 = new Sashimi();
        sashimi1.setId(1L);
        Sashimi sashimi2 = new Sashimi();
        sashimi2.setId(sashimi1.getId());
        assertThat(sashimi1).isEqualTo(sashimi2);
        sashimi2.setId(2L);
        assertThat(sashimi1).isNotEqualTo(sashimi2);
        sashimi1.setId(null);
        assertThat(sashimi1).isNotEqualTo(sashimi2);
    }
}
