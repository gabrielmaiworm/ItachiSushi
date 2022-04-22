package com.maiworm.sushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maiworm.sushi.IntegrationTest;
import com.maiworm.sushi.domain.Especiais;
import com.maiworm.sushi.repository.EspeciaisRepository;
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
 * Integration tests for the {@link EspeciaisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EspeciaisResourceIT {

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

    private static final String ENTITY_API_URL = "/api/especiais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EspeciaisRepository especiaisRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEspeciaisMockMvc;

    private Especiais especiais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especiais createEntity(EntityManager em) {
        Especiais especiais = new Especiais()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .imagem(DEFAULT_IMAGEM)
            .imagemContentType(DEFAULT_IMAGEM_CONTENT_TYPE)
            .preco(DEFAULT_PRECO)
            .promocao(DEFAULT_PROMOCAO);
        return especiais;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especiais createUpdatedEntity(EntityManager em) {
        Especiais especiais = new Especiais()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);
        return especiais;
    }

    @BeforeEach
    public void initTest() {
        especiais = createEntity(em);
    }

    @Test
    @Transactional
    void createEspeciais() throws Exception {
        int databaseSizeBeforeCreate = especiaisRepository.findAll().size();
        // Create the Especiais
        restEspeciaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(especiais)))
            .andExpect(status().isCreated());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeCreate + 1);
        Especiais testEspeciais = especiaisList.get(especiaisList.size() - 1);
        assertThat(testEspeciais.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEspeciais.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testEspeciais.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testEspeciais.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testEspeciais.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testEspeciais.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
    }

    @Test
    @Transactional
    void createEspeciaisWithExistingId() throws Exception {
        // Create the Especiais with an existing ID
        especiais.setId(1L);

        int databaseSizeBeforeCreate = especiaisRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspeciaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(especiais)))
            .andExpect(status().isBadRequest());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEspeciais() throws Exception {
        // Initialize the database
        especiaisRepository.saveAndFlush(especiais);

        // Get all the especiaisList
        restEspeciaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especiais.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].imagemContentType").value(hasItem(DEFAULT_IMAGEM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagem").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEM))))
            .andExpect(jsonPath("$.[*].preco").value(hasItem(DEFAULT_PRECO.doubleValue())))
            .andExpect(jsonPath("$.[*].promocao").value(hasItem(DEFAULT_PROMOCAO.booleanValue())));
    }

    @Test
    @Transactional
    void getEspeciais() throws Exception {
        // Initialize the database
        especiaisRepository.saveAndFlush(especiais);

        // Get the especiais
        restEspeciaisMockMvc
            .perform(get(ENTITY_API_URL_ID, especiais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(especiais.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.imagemContentType").value(DEFAULT_IMAGEM_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagem").value(Base64Utils.encodeToString(DEFAULT_IMAGEM)))
            .andExpect(jsonPath("$.preco").value(DEFAULT_PRECO.doubleValue()))
            .andExpect(jsonPath("$.promocao").value(DEFAULT_PROMOCAO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEspeciais() throws Exception {
        // Get the especiais
        restEspeciaisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEspeciais() throws Exception {
        // Initialize the database
        especiaisRepository.saveAndFlush(especiais);

        int databaseSizeBeforeUpdate = especiaisRepository.findAll().size();

        // Update the especiais
        Especiais updatedEspeciais = especiaisRepository.findById(especiais.getId()).get();
        // Disconnect from session so that the updates on updatedEspeciais are not directly saved in db
        em.detach(updatedEspeciais);
        updatedEspeciais
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restEspeciaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEspeciais.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEspeciais))
            )
            .andExpect(status().isOk());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeUpdate);
        Especiais testEspeciais = especiaisList.get(especiaisList.size() - 1);
        assertThat(testEspeciais.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEspeciais.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testEspeciais.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testEspeciais.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testEspeciais.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testEspeciais.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void putNonExistingEspeciais() throws Exception {
        int databaseSizeBeforeUpdate = especiaisRepository.findAll().size();
        especiais.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspeciaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, especiais.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(especiais))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEspeciais() throws Exception {
        int databaseSizeBeforeUpdate = especiaisRepository.findAll().size();
        especiais.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspeciaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(especiais))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEspeciais() throws Exception {
        int databaseSizeBeforeUpdate = especiaisRepository.findAll().size();
        especiais.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspeciaisMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(especiais)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEspeciaisWithPatch() throws Exception {
        // Initialize the database
        especiaisRepository.saveAndFlush(especiais);

        int databaseSizeBeforeUpdate = especiaisRepository.findAll().size();

        // Update the especiais using partial update
        Especiais partialUpdatedEspeciais = new Especiais();
        partialUpdatedEspeciais.setId(especiais.getId());

        partialUpdatedEspeciais
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .promocao(UPDATED_PROMOCAO);

        restEspeciaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspeciais.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEspeciais))
            )
            .andExpect(status().isOk());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeUpdate);
        Especiais testEspeciais = especiaisList.get(especiaisList.size() - 1);
        assertThat(testEspeciais.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEspeciais.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testEspeciais.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testEspeciais.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testEspeciais.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testEspeciais.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void fullUpdateEspeciaisWithPatch() throws Exception {
        // Initialize the database
        especiaisRepository.saveAndFlush(especiais);

        int databaseSizeBeforeUpdate = especiaisRepository.findAll().size();

        // Update the especiais using partial update
        Especiais partialUpdatedEspeciais = new Especiais();
        partialUpdatedEspeciais.setId(especiais.getId());

        partialUpdatedEspeciais
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restEspeciaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspeciais.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEspeciais))
            )
            .andExpect(status().isOk());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeUpdate);
        Especiais testEspeciais = especiaisList.get(especiaisList.size() - 1);
        assertThat(testEspeciais.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEspeciais.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testEspeciais.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testEspeciais.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testEspeciais.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testEspeciais.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void patchNonExistingEspeciais() throws Exception {
        int databaseSizeBeforeUpdate = especiaisRepository.findAll().size();
        especiais.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspeciaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, especiais.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(especiais))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEspeciais() throws Exception {
        int databaseSizeBeforeUpdate = especiaisRepository.findAll().size();
        especiais.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspeciaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(especiais))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEspeciais() throws Exception {
        int databaseSizeBeforeUpdate = especiaisRepository.findAll().size();
        especiais.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspeciaisMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(especiais))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especiais in the database
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEspeciais() throws Exception {
        // Initialize the database
        especiaisRepository.saveAndFlush(especiais);

        int databaseSizeBeforeDelete = especiaisRepository.findAll().size();

        // Delete the especiais
        restEspeciaisMockMvc
            .perform(delete(ENTITY_API_URL_ID, especiais.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Especiais> especiaisList = especiaisRepository.findAll();
        assertThat(especiaisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
