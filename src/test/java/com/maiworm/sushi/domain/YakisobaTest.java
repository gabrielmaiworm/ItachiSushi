package com.maiworm.sushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maiworm.sushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class YakisobaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Yakisoba.class);
        Yakisoba yakisoba1 = new Yakisoba();
        yakisoba1.setId(1L);
        Yakisoba yakisoba2 = new Yakisoba();
        yakisoba2.setId(yakisoba1.getId());
        assertThat(yakisoba1).isEqualTo(yakisoba2);
        yakisoba2.setId(2L);
        assertThat(yakisoba1).isNotEqualTo(yakisoba2);
        yakisoba1.setId(null);
        assertThat(yakisoba1).isNotEqualTo(yakisoba2);
    }
}
