package com.maiworm.sushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maiworm.sushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HarumakiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Harumaki.class);
        Harumaki harumaki1 = new Harumaki();
        harumaki1.setId(1L);
        Harumaki harumaki2 = new Harumaki();
        harumaki2.setId(harumaki1.getId());
        assertThat(harumaki1).isEqualTo(harumaki2);
        harumaki2.setId(2L);
        assertThat(harumaki1).isNotEqualTo(harumaki2);
        harumaki1.setId(null);
        assertThat(harumaki1).isNotEqualTo(harumaki2);
    }
}
