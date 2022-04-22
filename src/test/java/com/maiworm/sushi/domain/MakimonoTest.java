package com.maiworm.sushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maiworm.sushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MakimonoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Makimono.class);
        Makimono makimono1 = new Makimono();
        makimono1.setId(1L);
        Makimono makimono2 = new Makimono();
        makimono2.setId(makimono1.getId());
        assertThat(makimono1).isEqualTo(makimono2);
        makimono2.setId(2L);
        assertThat(makimono1).isNotEqualTo(makimono2);
        makimono1.setId(null);
        assertThat(makimono1).isNotEqualTo(makimono2);
    }
}
