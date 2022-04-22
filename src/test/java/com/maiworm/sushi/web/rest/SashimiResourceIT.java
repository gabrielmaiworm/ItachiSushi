package com.maiworm.sushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maiworm.sushi.IntegrationTest;
import com.maiworm.sushi.domain.Sashimi;
import com.maiworm.sushi.repository.SashimiRepository;
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
 * Integration tests for the {@link SashimiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SashimiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/sashimis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SashimiRepository sashimiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSashimiMockMvc;

    private Sashimi sashimi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sashimi createEntity(EntityManager em) {
        Sashimi sashimi = new Sashimi()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .imagem(DEFAULT_IMAGEM)
            .imagemContentType(DEFAULT_IMAGEM_CONTENT_TYPE)
            .preco(DEFAULT_PRECO)
            .promocao(DEFAULT_PROMOCAO)
            .ativo(DEFAULT_ATIVO);
        return sashimi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sashimi createUpdatedEntity(EntityManager em) {
        Sashimi sashimi = new Sashimi()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);
        return sashimi;
    }

    @BeforeEach
    public void initTest() {
        sashimi = createEntity(em);
    }

    @Test
    @Transactional
    void createSashimi() throws Exception {
        int databaseSizeBeforeCreate = sashimiRepository.findAll().size();
        // Create the Sashimi
        restSashimiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sashimi)))
            .andExpect(status().isCreated());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeCreate + 1);
        Sashimi testSashimi = sashimiList.get(sashimiList.size() - 1);
        assertThat(testSashimi.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testSashimi.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testSashimi.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testSashimi.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testSashimi.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testSashimi.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
        assertThat(testSashimi.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    void createSashimiWithExistingId() throws Exception {
        // Create the Sashimi with an existing ID
        sashimi.setId(1L);

        int databaseSizeBeforeCreate = sashimiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSashimiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sashimi)))
            .andExpect(status().isBadRequest());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSashimis() throws Exception {
        // Initialize the database
        sashimiRepository.saveAndFlush(sashimi);

        // Get all the sashimiList
        restSashimiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sashimi.getId().intValue())))
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
    void getSashimi() throws Exception {
        // Initialize the database
        sashimiRepository.saveAndFlush(sashimi);

        // Get the sashimi
        restSashimiMockMvc
            .perform(get(ENTITY_API_URL_ID, sashimi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sashimi.getId().intValue()))
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
    void getNonExistingSashimi() throws Exception {
        // Get the sashimi
        restSashimiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSashimi() throws Exception {
        // Initialize the database
        sashimiRepository.saveAndFlush(sashimi);

        int databaseSizeBeforeUpdate = sashimiRepository.findAll().size();

        // Update the sashimi
        Sashimi updatedSashimi = sashimiRepository.findById(sashimi.getId()).get();
        // Disconnect from session so that the updates on updatedSashimi are not directly saved in db
        em.detach(updatedSashimi);
        updatedSashimi
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);

        restSashimiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSashimi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSashimi))
            )
            .andExpect(status().isOk());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeUpdate);
        Sashimi testSashimi = sashimiList.get(sashimiList.size() - 1);
        assertThat(testSashimi.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSashimi.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testSashimi.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testSashimi.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testSashimi.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testSashimi.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testSashimi.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void putNonExistingSashimi() throws Exception {
        int databaseSizeBeforeUpdate = sashimiRepository.findAll().size();
        sashimi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSashimiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sashimi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sashimi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSashimi() throws Exception {
        int databaseSizeBeforeUpdate = sashimiRepository.findAll().size();
        sashimi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSashimiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sashimi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSashimi() throws Exception {
        int databaseSizeBeforeUpdate = sashimiRepository.findAll().size();
        sashimi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSashimiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sashimi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSashimiWithPatch() throws Exception {
        // Initialize the database
        sashimiRepository.saveAndFlush(sashimi);

        int databaseSizeBeforeUpdate = sashimiRepository.findAll().size();

        // Update the sashimi using partial update
        Sashimi partialUpdatedSashimi = new Sashimi();
        partialUpdatedSashimi.setId(sashimi.getId());

        partialUpdatedSashimi.nome(UPDATED_NOME).preco(UPDATED_PRECO);

        restSashimiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSashimi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSashimi))
            )
            .andExpect(status().isOk());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeUpdate);
        Sashimi testSashimi = sashimiList.get(sashimiList.size() - 1);
        assertThat(testSashimi.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSashimi.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testSashimi.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testSashimi.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testSashimi.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testSashimi.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
        assertThat(testSashimi.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    void fullUpdateSashimiWithPatch() throws Exception {
        // Initialize the database
        sashimiRepository.saveAndFlush(sashimi);

        int databaseSizeBeforeUpdate = sashimiRepository.findAll().size();

        // Update the sashimi using partial update
        Sashimi partialUpdatedSashimi = new Sashimi();
        partialUpdatedSashimi.setId(sashimi.getId());

        partialUpdatedSashimi
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);

        restSashimiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSashimi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSashimi))
            )
            .andExpect(status().isOk());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeUpdate);
        Sashimi testSashimi = sashimiList.get(sashimiList.size() - 1);
        assertThat(testSashimi.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSashimi.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testSashimi.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testSashimi.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testSashimi.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testSashimi.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testSashimi.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void patchNonExistingSashimi() throws Exception {
        int databaseSizeBeforeUpdate = sashimiRepository.findAll().size();
        sashimi.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSashimiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sashimi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sashimi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSashimi() throws Exception {
        int databaseSizeBeforeUpdate = sashimiRepository.findAll().size();
        sashimi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSashimiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sashimi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSashimi() throws Exception {
        int databaseSizeBeforeUpdate = sashimiRepository.findAll().size();
        sashimi.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSashimiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sashimi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sashimi in the database
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSashimi() throws Exception {
        // Initialize the database
        sashimiRepository.saveAndFlush(sashimi);

        int databaseSizeBeforeDelete = sashimiRepository.findAll().size();

        // Delete the sashimi
        restSashimiMockMvc
            .perform(delete(ENTITY_API_URL_ID, sashimi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sashimi> sashimiList = sashimiRepository.findAll();
        assertThat(sashimiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
