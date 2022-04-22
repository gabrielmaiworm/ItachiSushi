package com.maiworm.sushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maiworm.sushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TemakiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Temaki.class);
        Temaki temaki1 = new Temaki();
        temaki1.setId(1L);
        Temaki temaki2 = new Temaki();
        temaki2.setId(temaki1.getId());
        assertThat(temaki1).isEqualTo(temaki2);
        temaki2.setId(2L);
        assertThat(temaki1).isNotEqualTo(temaki2);
        temaki1.setId(null);
        assertThat(temaki1).isNotEqualTo(temaki2);
    }
}
