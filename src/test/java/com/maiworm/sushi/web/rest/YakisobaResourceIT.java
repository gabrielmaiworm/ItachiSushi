package com.maiworm.sushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maiworm.sushi.IntegrationTest;
import com.maiworm.sushi.domain.Yakisoba;
import com.maiworm.sushi.repository.YakisobaRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link YakisobaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class YakisobaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGEM = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEM = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGEM_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGEM_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_PRECO = 1D;
    private static final Double UPDATED_PRECO = 2D;

    private static final Boolean DEFAULT_PROMOCAO = false;
    private static final Boolean UPDATED_PROMOCAO = true;

    private static final String ENTITY_API_URL = "/api/yakisobas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private YakisobaRepository yakisobaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restYakisobaMockMvc;

    private Yakisoba yakisoba;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Yakisoba createEntity(EntityManager em) {
        Yakisoba yakisoba = new Yakisoba()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .imagem(DEFAULT_IMAGEM)
            .imagemContentType(DEFAULT_IMAGEM_CONTENT_TYPE)
            .preco(DEFAULT_PRECO)
            .promocao(DEFAULT_PROMOCAO);
        return yakisoba;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Yakisoba createUpdatedEntity(EntityManager em) {
        Yakisoba yakisoba = new Yakisoba()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);
        return yakisoba;
    }

    @BeforeEach
    public void initTest() {
        yakisoba = createEntity(em);
    }

    @Test
    @Transactional
    void createYakisoba() throws Exception {
        int databaseSizeBeforeCreate = yakisobaRepository.findAll().size();
        // Create the Yakisoba
        restYakisobaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(yakisoba)))
            .andExpect(status().isCreated());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeCreate + 1);
        Yakisoba testYakisoba = yakisobaList.get(yakisobaList.size() - 1);
        assertThat(testYakisoba.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testYakisoba.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testYakisoba.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testYakisoba.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testYakisoba.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testYakisoba.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
    }

    @Test
    @Transactional
    void createYakisobaWithExistingId() throws Exception {
        // Create the Yakisoba with an existing ID
        yakisoba.setId(1L);

        int databaseSizeBeforeCreate = yakisobaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restYakisobaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(yakisoba)))
            .andExpect(status().isBadRequest());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllYakisobas() throws Exception {
        // Initialize the database
        yakisobaRepository.saveAndFlush(yakisoba);

        // Get all the yakisobaList
        restYakisobaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(yakisoba.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].imagemContentType").value(hasItem(DEFAULT_IMAGEM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagem").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEM))))
            .andExpect(jsonPath("$.[*].preco").value(hasItem(DEFAULT_PRECO.doubleValue())))
            .andExpect(jsonPath("$.[*].promocao").value(hasItem(DEFAULT_PROMOCAO.booleanValue())));
    }

    @Test
    @Transactional
    void getYakisoba() throws Exception {
        // Initialize the database
        yakisobaRepository.saveAndFlush(yakisoba);

        // Get the yakisoba
        restYakisobaMockMvc
            .perform(get(ENTITY_API_URL_ID, yakisoba.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(yakisoba.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.imagemContentType").value(DEFAULT_IMAGEM_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagem").value(Base64Utils.encodeToString(DEFAULT_IMAGEM)))
            .andExpect(jsonPath("$.preco").value(DEFAULT_PRECO.doubleValue()))
            .andExpect(jsonPath("$.promocao").value(DEFAULT_PROMOCAO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingYakisoba() throws Exception {
        // Get the yakisoba
        restYakisobaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewYakisoba() throws Exception {
        // Initialize the database
        yakisobaRepository.saveAndFlush(yakisoba);

        int databaseSizeBeforeUpdate = yakisobaRepository.findAll().size();

        // Update the yakisoba
        Yakisoba updatedYakisoba = yakisobaRepository.findById(yakisoba.getId()).get();
        // Disconnect from session so that the updates on updatedYakisoba are not directly saved in db
        em.detach(updatedYakisoba);
        updatedYakisoba
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restYakisobaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedYakisoba.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedYakisoba))
            )
            .andExpect(status().isOk());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeUpdate);
        Yakisoba testYakisoba = yakisobaList.get(yakisobaList.size() - 1);
        assertThat(testYakisoba.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testYakisoba.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testYakisoba.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testYakisoba.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testYakisoba.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testYakisoba.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void putNonExistingYakisoba() throws Exception {
        int databaseSizeBeforeUpdate = yakisobaRepository.findAll().size();
        yakisoba.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restYakisobaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, yakisoba.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(yakisoba))
            )
            .andExpect(status().isBadRequest());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchYakisoba() throws Exception {
        int databaseSizeBeforeUpdate = yakisobaRepository.findAll().size();
        yakisoba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYakisobaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(yakisoba))
            )
            .andExpect(status().isBadRequest());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamYakisoba() throws Exception {
        int databaseSizeBeforeUpdate = yakisobaRepository.findAll().size();
        yakisoba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYakisobaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(yakisoba)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateYakisobaWithPatch() throws Exception {
        // Initialize the database
        yakisobaRepository.saveAndFlush(yakisoba);

        int databaseSizeBeforeUpdate = yakisobaRepository.findAll().size();

        // Update the yakisoba using partial update
        Yakisoba partialUpdatedYakisoba = new Yakisoba();
        partialUpdatedYakisoba.setId(yakisoba.getId());

        partialUpdatedYakisoba.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restYakisobaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedYakisoba.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedYakisoba))
            )
            .andExpect(status().isOk());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeUpdate);
        Yakisoba testYakisoba = yakisobaList.get(yakisobaList.size() - 1);
        assertThat(testYakisoba.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testYakisoba.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testYakisoba.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testYakisoba.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testYakisoba.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testYakisoba.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
    }

    @Test
    @Transactional
    void fullUpdateYakisobaWithPatch() throws Exception {
        // Initialize the database
        yakisobaRepository.saveAndFlush(yakisoba);

        int databaseSizeBeforeUpdate = yakisobaRepository.findAll().size();

        // Update the yakisoba using partial update
        Yakisoba partialUpdatedYakisoba = new Yakisoba();
        partialUpdatedYakisoba.setId(yakisoba.getId());

        partialUpdatedYakisoba
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restYakisobaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedYakisoba.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedYakisoba))
            )
            .andExpect(status().isOk());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeUpdate);
        Yakisoba testYakisoba = yakisobaList.get(yakisobaList.size() - 1);
        assertThat(testYakisoba.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testYakisoba.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testYakisoba.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testYakisoba.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testYakisoba.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testYakisoba.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void patchNonExistingYakisoba() throws Exception {
        int databaseSizeBeforeUpdate = yakisobaRepository.findAll().size();
        yakisoba.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restYakisobaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, yakisoba.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(yakisoba))
            )
            .andExpect(status().isBadRequest());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchYakisoba() throws Exception {
        int databaseSizeBeforeUpdate = yakisobaRepository.findAll().size();
        yakisoba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYakisobaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(yakisoba))
            )
            .andExpect(status().isBadRequest());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamYakisoba() throws Exception {
        int databaseSizeBeforeUpdate = yakisobaRepository.findAll().size();
        yakisoba.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYakisobaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(yakisoba)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Yakisoba in the database
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteYakisoba() throws Exception {
        // Initialize the database
        yakisobaRepository.saveAndFlush(yakisoba);

        int databaseSizeBeforeDelete = yakisobaRepository.findAll().size();

        // Delete the yakisoba
        restYakisobaMockMvc
            .perform(delete(ENTITY_API_URL_ID, yakisoba.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Yakisoba> yakisobaList = yakisobaRepository.findAll();
        assertThat(yakisobaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
