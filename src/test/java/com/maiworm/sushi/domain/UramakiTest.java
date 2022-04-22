package com.maiworm.sushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maiworm.sushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UramakiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Uramaki.class);
        Uramaki uramaki1 = new Uramaki();
        uramaki1.setId(1L);
        Uramaki uramaki2 = new Uramaki();
        uramaki2.setId(uramaki1.getId());
        assertThat(uramaki1).isEqualTo(uramaki2);
        uramaki2.setId(2L);
        assertThat(uramaki1).isNotEqualTo(uramaki2);
        uramaki1.setId(null);
        assertThat(uramaki1).isNotEqualTo(uramaki2);
    }
}
