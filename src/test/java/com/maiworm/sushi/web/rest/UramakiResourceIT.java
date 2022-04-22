package com.maiworm.sushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maiworm.sushi.IntegrationTest;
import com.maiworm.sushi.domain.Uramaki;
import com.maiworm.sushi.repository.UramakiRepository;
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
 * Integration tests for the {@link UramakiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UramakiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/uramakis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UramakiRepository uramakiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUramakiMockMvc;

    private Uramaki uramaki;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Uramaki createEntity(EntityManager em) {
        Uramaki uramaki = new Uramaki()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .imagem(DEFAULT_IMAGEM)
            .imagemContentType(DEFAULT_IMAGEM_CONTENT_TYPE)
            .preco(DEFAULT_PRECO)
            .promocao(DEFAULT_PROMOCAO);
        return uramaki;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Uramaki createUpdatedEntity(EntityManager em) {
        Uramaki uramaki = new Uramaki()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);
        return uramaki;
    }

    @BeforeEach
    public void initTest() {
        uramaki = createEntity(em);
    }

    @Test
    @Transactional
    void createUramaki() throws Exception {
        int databaseSizeBeforeCreate = uramakiRepository.findAll().size();
        // Create the Uramaki
        restUramakiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uramaki)))
            .andExpect(status().isCreated());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeCreate + 1);
        Uramaki testUramaki = uramakiList.get(uramakiList.size() - 1);
        assertThat(testUramaki.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testUramaki.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testUramaki.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testUramaki.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testUramaki.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testUramaki.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
    }

    @Test
    @Transactional
    void createUramakiWithExistingId() throws Exception {
        // Create the Uramaki with an existing ID
        uramaki.setId(1L);

        int databaseSizeBeforeCreate = uramakiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUramakiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uramaki)))
            .andExpect(status().isBadRequest());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUramakis() throws Exception {
        // Initialize the database
        uramakiRepository.saveAndFlush(uramaki);

        // Get all the uramakiList
        restUramakiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uramaki.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].imagemContentType").value(hasItem(DEFAULT_IMAGEM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagem").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEM))))
            .andExpect(jsonPath("$.[*].preco").value(hasItem(DEFAULT_PRECO.doubleValue())))
            .andExpect(jsonPath("$.[*].promocao").value(hasItem(DEFAULT_PROMOCAO.booleanValue())));
    }

    @Test
    @Transactional
    void getUramaki() throws Exception {
        // Initialize the database
        uramakiRepository.saveAndFlush(uramaki);

        // Get the uramaki
        restUramakiMockMvc
            .perform(get(ENTITY_API_URL_ID, uramaki.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uramaki.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.imagemContentType").value(DEFAULT_IMAGEM_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagem").value(Base64Utils.encodeToString(DEFAULT_IMAGEM)))
            .andExpect(jsonPath("$.preco").value(DEFAULT_PRECO.doubleValue()))
            .andExpect(jsonPath("$.promocao").value(DEFAULT_PROMOCAO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUramaki() throws Exception {
        // Get the uramaki
        restUramakiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUramaki() throws Exception {
        // Initialize the database
        uramakiRepository.saveAndFlush(uramaki);

        int databaseSizeBeforeUpdate = uramakiRepository.findAll().size();

        // Update the uramaki
        Uramaki updatedUramaki = uramakiRepository.findById(uramaki.getId()).get();
        // Disconnect from session so that the updates on updatedUramaki are not directly saved in db
        em.detach(updatedUramaki);
        updatedUramaki
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restUramakiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUramaki.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUramaki))
            )
            .andExpect(status().isOk());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeUpdate);
        Uramaki testUramaki = uramakiList.get(uramakiList.size() - 1);
        assertThat(testUramaki.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUramaki.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testUramaki.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testUramaki.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testUramaki.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testUramaki.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void putNonExistingUramaki() throws Exception {
        int databaseSizeBeforeUpdate = uramakiRepository.findAll().size();
        uramaki.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUramakiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uramaki.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uramaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUramaki() throws Exception {
        int databaseSizeBeforeUpdate = uramakiRepository.findAll().size();
        uramaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUramakiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uramaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUramaki() throws Exception {
        int databaseSizeBeforeUpdate = uramakiRepository.findAll().size();
        uramaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUramakiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uramaki)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUramakiWithPatch() throws Exception {
        // Initialize the database
        uramakiRepository.saveAndFlush(uramaki);

        int databaseSizeBeforeUpdate = uramakiRepository.findAll().size();

        // Update the uramaki using partial update
        Uramaki partialUpdatedUramaki = new Uramaki();
        partialUpdatedUramaki.setId(uramaki.getId());

        partialUpdatedUramaki
            .nome(UPDATED_NOME)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restUramakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUramaki.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUramaki))
            )
            .andExpect(status().isOk());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeUpdate);
        Uramaki testUramaki = uramakiList.get(uramakiList.size() - 1);
        assertThat(testUramaki.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUramaki.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testUramaki.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testUramaki.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testUramaki.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testUramaki.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void fullUpdateUramakiWithPatch() throws Exception {
        // Initialize the database
        uramakiRepository.saveAndFlush(uramaki);

        int databaseSizeBeforeUpdate = uramakiRepository.findAll().size();

        // Update the uramaki using partial update
        Uramaki partialUpdatedUramaki = new Uramaki();
        partialUpdatedUramaki.setId(uramaki.getId());

        partialUpdatedUramaki
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restUramakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUramaki.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUramaki))
            )
            .andExpect(status().isOk());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeUpdate);
        Uramaki testUramaki = uramakiList.get(uramakiList.size() - 1);
        assertThat(testUramaki.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUramaki.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testUramaki.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testUramaki.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testUramaki.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testUramaki.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void patchNonExistingUramaki() throws Exception {
        int databaseSizeBeforeUpdate = uramakiRepository.findAll().size();
        uramaki.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUramakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uramaki.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uramaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUramaki() throws Exception {
        int databaseSizeBeforeUpdate = uramakiRepository.findAll().size();
        uramaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUramakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uramaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUramaki() throws Exception {
        int databaseSizeBeforeUpdate = uramakiRepository.findAll().size();
        uramaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUramakiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(uramaki)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Uramaki in the database
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUramaki() throws Exception {
        // Initialize the database
        uramakiRepository.saveAndFlush(uramaki);

        int databaseSizeBeforeDelete = uramakiRepository.findAll().size();

        // Delete the uramaki
        restUramakiMockMvc
            .perform(delete(ENTITY_API_URL_ID, uramaki.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Uramaki> uramakiList = uramakiRepository.findAll();
        assertThat(uramakiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
