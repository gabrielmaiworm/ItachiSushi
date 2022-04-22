package com.maiworm.sushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maiworm.sushi.IntegrationTest;
import com.maiworm.sushi.domain.Sushi;
import com.maiworm.sushi.repository.SushiRepository;
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
 * Integration tests for the {@link SushiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SushiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/sushis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SushiRepository sushiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSushiMockMvc;

    private Sushi sushi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sushi createEntity(EntityManager em) {
        Sushi sushi = new Sushi()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .imagem(DEFAULT_IMAGEM)
            .imagemContentType(DEFAULT_IMAGEM_CONTENT_TYPE)
            .preco(DEFAULT_PRECO)
            .promocao(DEFAULT_PROMOCAO)
            .ativo(DEFAULT_ATIVO);
        return sushi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sushi createUpdatedEntity(EntityManager em) {
        Sushi sushi = new Sushi()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);
        return sushi;
    }

    @BeforeEach
    public void initTest() {
        sushi = createEntity(em);
    }

    @Test
    @Transactional
    void createSushi() throws Exception {
        int databaseSizeBeforeCreate = sushiRepository.findAll().size();
        // Create the Sushi
        restSushiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sushi)))
            .andExpect(status().isCreated());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeCreate + 1);
        Sushi testSushi = sushiList.get(sushiList.size() - 1);
        assertThat(testSushi.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testSushi.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testSushi.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testSushi.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testSushi.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testSushi.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
        assertThat(testSushi.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    void createSushiWithExistingId() throws Exception {
        // Create the Sushi with an existing ID
        sushi.setId(1L);

        int databaseSizeBeforeCreate = sushiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSushiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sushi)))
            .andExpect(status().isBadRequest());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSushis() throws Exception {
        // Initialize the database
        sushiRepository.saveAndFlush(sushi);

        // Get all the sushiList
        restSushiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sushi.getId().intValue())))
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
    void getSushi() throws Exception {
        // Initialize the database
        sushiRepository.saveAndFlush(sushi);

        // Get the sushi
        restSushiMockMvc
            .perform(get(ENTITY_API_URL_ID, sushi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sushi.getId().intValue()))
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
    void getNonExistingSushi() throws Exception {
        // Get the sushi
        restSushiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSushi() throws Exception {
        // Initialize the database
        sushiRepository.saveAndFlush(sushi);

        int databaseSizeBeforeUpdate = sushiRepository.findAll().size();

        // Update the sushi
        Sushi updatedSushi = sushiRepository.findById(sushi.getId()).get();
        // Disconnect from session so that the updates on updatedSushi are not directly saved in db
        em.detach(updatedSushi);
        updatedSushi
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);

        restSushiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSushi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSushi))
            )
            .andExpect(status().isOk());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeUpdate);
        Sushi testSushi = sushiList.get(sushiList.size() - 1);
        assertThat(testSushi.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSushi.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testSushi.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testSushi.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testSushi.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testSushi.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testSushi.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void putNonExistingSushi() throws Exception {
        int databaseSizeBeforeUpdate = sushiRepository.findAll().size();
        sushi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSushiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sushi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sushi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSushi() throws Exception {
        int databaseSizeBeforeUpdate = sushiRepository.findAll().size();
        sushi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSushiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sushi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSushi() throws Exception {
        int databaseSizeBeforeUpdate = sushiRepository.findAll().size();
        sushi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSushiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sushi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSushiWithPatch() throws Exception {
        // Initialize the database
        sushiRepository.saveAndFlush(sushi);

        int databaseSizeBeforeUpdate = sushiRepository.findAll().size();

        // Update the sushi using partial update
        Sushi partialUpdatedSushi = new Sushi();
        partialUpdatedSushi.setId(sushi.getId());

        partialUpdatedSushi.nome(UPDATED_NOME).imagem(UPDATED_IMAGEM).imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE).preco(UPDATED_PRECO);

        restSushiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSushi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSushi))
            )
            .andExpect(status().isOk());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeUpdate);
        Sushi testSushi = sushiList.get(sushiList.size() - 1);
        assertThat(testSushi.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSushi.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testSushi.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testSushi.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testSushi.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testSushi.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
        assertThat(testSushi.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    void fullUpdateSushiWithPatch() throws Exception {
        // Initialize the database
        sushiRepository.saveAndFlush(sushi);

        int databaseSizeBeforeUpdate = sushiRepository.findAll().size();

        // Update the sushi using partial update
        Sushi partialUpdatedSushi = new Sushi();
        partialUpdatedSushi.setId(sushi.getId());

        partialUpdatedSushi
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);

        restSushiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSushi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSushi))
            )
            .andExpect(status().isOk());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeUpdate);
        Sushi testSushi = sushiList.get(sushiList.size() - 1);
        assertThat(testSushi.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSushi.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testSushi.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testSushi.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testSushi.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testSushi.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testSushi.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void patchNonExistingSushi() throws Exception {
        int databaseSizeBeforeUpdate = sushiRepository.findAll().size();
        sushi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSushiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sushi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sushi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSushi() throws Exception {
        int databaseSizeBeforeUpdate = sushiRepository.findAll().size();
        sushi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSushiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sushi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSushi() throws Exception {
        int databaseSizeBeforeUpdate = sushiRepository.findAll().size();
        sushi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSushiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sushi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sushi in the database
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSushi() throws Exception {
        // Initialize the database
        sushiRepository.saveAndFlush(sushi);

        int databaseSizeBeforeDelete = sushiRepository.findAll().size();

        // Delete the sushi
        restSushiMockMvc
            .perform(delete(ENTITY_API_URL_ID, sushi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sushi> sushiList = sushiRepository.findAll();
        assertThat(sushiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
