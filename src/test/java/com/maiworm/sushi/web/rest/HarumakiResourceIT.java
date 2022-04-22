package com.maiworm.sushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maiworm.sushi.IntegrationTest;
import com.maiworm.sushi.domain.Harumaki;
import com.maiworm.sushi.repository.HarumakiRepository;
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
 * Integration tests for the {@link HarumakiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HarumakiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/harumakis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HarumakiRepository harumakiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHarumakiMockMvc;

    private Harumaki harumaki;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Harumaki createEntity(EntityManager em) {
        Harumaki harumaki = new Harumaki()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .imagem(DEFAULT_IMAGEM)
            .imagemContentType(DEFAULT_IMAGEM_CONTENT_TYPE)
            .preco(DEFAULT_PRECO)
            .promocao(DEFAULT_PROMOCAO);
        return harumaki;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Harumaki createUpdatedEntity(EntityManager em) {
        Harumaki harumaki = new Harumaki()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);
        return harumaki;
    }

    @BeforeEach
    public void initTest() {
        harumaki = createEntity(em);
    }

    @Test
    @Transactional
    void createHarumaki() throws Exception {
        int databaseSizeBeforeCreate = harumakiRepository.findAll().size();
        // Create the Harumaki
        restHarumakiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(harumaki)))
            .andExpect(status().isCreated());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeCreate + 1);
        Harumaki testHarumaki = harumakiList.get(harumakiList.size() - 1);
        assertThat(testHarumaki.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testHarumaki.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testHarumaki.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testHarumaki.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testHarumaki.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testHarumaki.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
    }

    @Test
    @Transactional
    void createHarumakiWithExistingId() throws Exception {
        // Create the Harumaki with an existing ID
        harumaki.setId(1L);

        int databaseSizeBeforeCreate = harumakiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHarumakiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(harumaki)))
            .andExpect(status().isBadRequest());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHarumakis() throws Exception {
        // Initialize the database
        harumakiRepository.saveAndFlush(harumaki);

        // Get all the harumakiList
        restHarumakiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(harumaki.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].imagemContentType").value(hasItem(DEFAULT_IMAGEM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagem").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEM))))
            .andExpect(jsonPath("$.[*].preco").value(hasItem(DEFAULT_PRECO.doubleValue())))
            .andExpect(jsonPath("$.[*].promocao").value(hasItem(DEFAULT_PROMOCAO.booleanValue())));
    }

    @Test
    @Transactional
    void getHarumaki() throws Exception {
        // Initialize the database
        harumakiRepository.saveAndFlush(harumaki);

        // Get the harumaki
        restHarumakiMockMvc
            .perform(get(ENTITY_API_URL_ID, harumaki.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(harumaki.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.imagemContentType").value(DEFAULT_IMAGEM_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagem").value(Base64Utils.encodeToString(DEFAULT_IMAGEM)))
            .andExpect(jsonPath("$.preco").value(DEFAULT_PRECO.doubleValue()))
            .andExpect(jsonPath("$.promocao").value(DEFAULT_PROMOCAO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHarumaki() throws Exception {
        // Get the harumaki
        restHarumakiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHarumaki() throws Exception {
        // Initialize the database
        harumakiRepository.saveAndFlush(harumaki);

        int databaseSizeBeforeUpdate = harumakiRepository.findAll().size();

        // Update the harumaki
        Harumaki updatedHarumaki = harumakiRepository.findById(harumaki.getId()).get();
        // Disconnect from session so that the updates on updatedHarumaki are not directly saved in db
        em.detach(updatedHarumaki);
        updatedHarumaki
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restHarumakiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHarumaki.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHarumaki))
            )
            .andExpect(status().isOk());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeUpdate);
        Harumaki testHarumaki = harumakiList.get(harumakiList.size() - 1);
        assertThat(testHarumaki.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testHarumaki.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testHarumaki.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testHarumaki.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testHarumaki.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testHarumaki.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void putNonExistingHarumaki() throws Exception {
        int databaseSizeBeforeUpdate = harumakiRepository.findAll().size();
        harumaki.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHarumakiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, harumaki.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(harumaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHarumaki() throws Exception {
        int databaseSizeBeforeUpdate = harumakiRepository.findAll().size();
        harumaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarumakiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(harumaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHarumaki() throws Exception {
        int databaseSizeBeforeUpdate = harumakiRepository.findAll().size();
        harumaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarumakiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(harumaki)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHarumakiWithPatch() throws Exception {
        // Initialize the database
        harumakiRepository.saveAndFlush(harumaki);

        int databaseSizeBeforeUpdate = harumakiRepository.findAll().size();

        // Update the harumaki using partial update
        Harumaki partialUpdatedHarumaki = new Harumaki();
        partialUpdatedHarumaki.setId(harumaki.getId());

        partialUpdatedHarumaki.imagem(UPDATED_IMAGEM).imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE).preco(UPDATED_PRECO);

        restHarumakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHarumaki.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHarumaki))
            )
            .andExpect(status().isOk());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeUpdate);
        Harumaki testHarumaki = harumakiList.get(harumakiList.size() - 1);
        assertThat(testHarumaki.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testHarumaki.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testHarumaki.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testHarumaki.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testHarumaki.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testHarumaki.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
    }

    @Test
    @Transactional
    void fullUpdateHarumakiWithPatch() throws Exception {
        // Initialize the database
        harumakiRepository.saveAndFlush(harumaki);

        int databaseSizeBeforeUpdate = harumakiRepository.findAll().size();

        // Update the harumaki using partial update
        Harumaki partialUpdatedHarumaki = new Harumaki();
        partialUpdatedHarumaki.setId(harumaki.getId());

        partialUpdatedHarumaki
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restHarumakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHarumaki.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHarumaki))
            )
            .andExpect(status().isOk());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeUpdate);
        Harumaki testHarumaki = harumakiList.get(harumakiList.size() - 1);
        assertThat(testHarumaki.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testHarumaki.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testHarumaki.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testHarumaki.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testHarumaki.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testHarumaki.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void patchNonExistingHarumaki() throws Exception {
        int databaseSizeBeforeUpdate = harumakiRepository.findAll().size();
        harumaki.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHarumakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, harumaki.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(harumaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHarumaki() throws Exception {
        int databaseSizeBeforeUpdate = harumakiRepository.findAll().size();
        harumaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarumakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(harumaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHarumaki() throws Exception {
        int databaseSizeBeforeUpdate = harumakiRepository.findAll().size();
        harumaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHarumakiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(harumaki)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Harumaki in the database
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHarumaki() throws Exception {
        // Initialize the database
        harumakiRepository.saveAndFlush(harumaki);

        int databaseSizeBeforeDelete = harumakiRepository.findAll().size();

        // Delete the harumaki
        restHarumakiMockMvc
            .perform(delete(ENTITY_API_URL_ID, harumaki.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Harumaki> harumakiList = harumakiRepository.findAll();
        assertThat(harumakiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
