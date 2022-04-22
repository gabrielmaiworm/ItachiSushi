package com.maiworm.sushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maiworm.sushi.IntegrationTest;
import com.maiworm.sushi.domain.Temaki;
import com.maiworm.sushi.repository.TemakiRepository;
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
 * Integration tests for the {@link TemakiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TemakiResourceIT {

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

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/temakis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemakiRepository temakiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemakiMockMvc;

    private Temaki temaki;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temaki createEntity(EntityManager em) {
        Temaki temaki = new Temaki()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .imagem(DEFAULT_IMAGEM)
            .imagemContentType(DEFAULT_IMAGEM_CONTENT_TYPE)
            .preco(DEFAULT_PRECO)
            .promocao(DEFAULT_PROMOCAO)
            .ativo(DEFAULT_ATIVO);
        return temaki;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temaki createUpdatedEntity(EntityManager em) {
        Temaki temaki = new Temaki()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);
        return temaki;
    }

    @BeforeEach
    public void initTest() {
        temaki = createEntity(em);
    }

    @Test
    @Transactional
    void createTemaki() throws Exception {
        int databaseSizeBeforeCreate = temakiRepository.findAll().size();
        // Create the Temaki
        restTemakiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(temaki)))
            .andExpect(status().isCreated());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeCreate + 1);
        Temaki testTemaki = temakiList.get(temakiList.size() - 1);
        assertThat(testTemaki.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTemaki.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTemaki.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testTemaki.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testTemaki.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testTemaki.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
        assertThat(testTemaki.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    void createTemakiWithExistingId() throws Exception {
        // Create the Temaki with an existing ID
        temaki.setId(1L);

        int databaseSizeBeforeCreate = temakiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemakiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(temaki)))
            .andExpect(status().isBadRequest());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTemakis() throws Exception {
        // Initialize the database
        temakiRepository.saveAndFlush(temaki);

        // Get all the temakiList
        restTemakiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temaki.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].imagemContentType").value(hasItem(DEFAULT_IMAGEM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagem").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEM))))
            .andExpect(jsonPath("$.[*].preco").value(hasItem(DEFAULT_PRECO.doubleValue())))
            .andExpect(jsonPath("$.[*].promocao").value(hasItem(DEFAULT_PROMOCAO.booleanValue())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }

    @Test
    @Transactional
    void getTemaki() throws Exception {
        // Initialize the database
        temakiRepository.saveAndFlush(temaki);

        // Get the temaki
        restTemakiMockMvc
            .perform(get(ENTITY_API_URL_ID, temaki.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(temaki.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.imagemContentType").value(DEFAULT_IMAGEM_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagem").value(Base64Utils.encodeToString(DEFAULT_IMAGEM)))
            .andExpect(jsonPath("$.preco").value(DEFAULT_PRECO.doubleValue()))
            .andExpect(jsonPath("$.promocao").value(DEFAULT_PROMOCAO.booleanValue()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTemaki() throws Exception {
        // Get the temaki
        restTemakiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTemaki() throws Exception {
        // Initialize the database
        temakiRepository.saveAndFlush(temaki);

        int databaseSizeBeforeUpdate = temakiRepository.findAll().size();

        // Update the temaki
        Temaki updatedTemaki = temakiRepository.findById(temaki.getId()).get();
        // Disconnect from session so that the updates on updatedTemaki are not directly saved in db
        em.detach(updatedTemaki);
        updatedTemaki
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);

        restTemakiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTemaki.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTemaki))
            )
            .andExpect(status().isOk());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeUpdate);
        Temaki testTemaki = temakiList.get(temakiList.size() - 1);
        assertThat(testTemaki.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTemaki.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTemaki.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testTemaki.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testTemaki.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testTemaki.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testTemaki.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void putNonExistingTemaki() throws Exception {
        int databaseSizeBeforeUpdate = temakiRepository.findAll().size();
        temaki.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemakiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, temaki.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(temaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemaki() throws Exception {
        int databaseSizeBeforeUpdate = temakiRepository.findAll().size();
        temaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemakiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(temaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemaki() throws Exception {
        int databaseSizeBeforeUpdate = temakiRepository.findAll().size();
        temaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemakiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(temaki)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemakiWithPatch() throws Exception {
        // Initialize the database
        temakiRepository.saveAndFlush(temaki);

        int databaseSizeBeforeUpdate = temakiRepository.findAll().size();

        // Update the temaki using partial update
        Temaki partialUpdatedTemaki = new Temaki();
        partialUpdatedTemaki.setId(temaki.getId());

        partialUpdatedTemaki
            .nome(UPDATED_NOME)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);

        restTemakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemaki.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemaki))
            )
            .andExpect(status().isOk());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeUpdate);
        Temaki testTemaki = temakiList.get(temakiList.size() - 1);
        assertThat(testTemaki.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTemaki.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTemaki.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testTemaki.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testTemaki.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testTemaki.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testTemaki.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void fullUpdateTemakiWithPatch() throws Exception {
        // Initialize the database
        temakiRepository.saveAndFlush(temaki);

        int databaseSizeBeforeUpdate = temakiRepository.findAll().size();

        // Update the temaki using partial update
        Temaki partialUpdatedTemaki = new Temaki();
        partialUpdatedTemaki.setId(temaki.getId());

        partialUpdatedTemaki
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);

        restTemakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemaki.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemaki))
            )
            .andExpect(status().isOk());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeUpdate);
        Temaki testTemaki = temakiList.get(temakiList.size() - 1);
        assertThat(testTemaki.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTemaki.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTemaki.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testTemaki.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testTemaki.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testTemaki.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testTemaki.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void patchNonExistingTemaki() throws Exception {
        int databaseSizeBeforeUpdate = temakiRepository.findAll().size();
        temaki.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, temaki.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(temaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemaki() throws Exception {
        int databaseSizeBeforeUpdate = temakiRepository.findAll().size();
        temaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemakiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(temaki))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemaki() throws Exception {
        int databaseSizeBeforeUpdate = temakiRepository.findAll().size();
        temaki.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemakiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(temaki)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Temaki in the database
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemaki() throws Exception {
        // Initialize the database
        temakiRepository.saveAndFlush(temaki);

        int databaseSizeBeforeDelete = temakiRepository.findAll().size();

        // Delete the temaki
        restTemakiMockMvc
            .perform(delete(ENTITY_API_URL_ID, temaki.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Temaki> temakiList = temakiRepository.findAll();
        assertThat(temakiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
