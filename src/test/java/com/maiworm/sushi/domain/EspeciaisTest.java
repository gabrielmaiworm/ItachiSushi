package com.maiworm.sushi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maiworm.sushi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EspeciaisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Especiais.class);
        Especiais especiais1 = new Especiais();
        especiais1.setId(1L);
        Especiais especiais2 = new Especiais();
        especiais2.setId(especiais1.getId());
        assertThat(especiais1).isEqualTo(especiais2);
        especiais2.setId(2L);
        assertThat(especiais1).isNotEqualTo(especiais2);
        especiais1.setId(null);
        assertThat(especiais1).isNotEqualTo(especiais2);
    }
}
