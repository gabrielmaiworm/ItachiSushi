package com.maiworm.sushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maiworm.sushi.IntegrationTest;
import com.maiworm.sushi.domain.Makimono;
import com.maiworm.sushi.repository.MakimonoRepository;
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
 * Integration tests for the {@link MakimonoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MakimonoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/makimonos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MakimonoRepository makimonoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMakimonoMockMvc;

    private Makimono makimono;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Makimono createEntity(EntityManager em) {
        Makimono makimono = new Makimono()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .imagem(DEFAULT_IMAGEM)
            .imagemContentType(DEFAULT_IMAGEM_CONTENT_TYPE)
            .preco(DEFAULT_PRECO)
            .promocao(DEFAULT_PROMOCAO)
            .ativo(DEFAULT_ATIVO);
        return makimono;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Makimono createUpdatedEntity(EntityManager em) {
        Makimono makimono = new Makimono()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);
        return makimono;
    }

    @BeforeEach
    public void initTest() {
        makimono = createEntity(em);
    }

    @Test
    @Transactional
    void createMakimono() throws Exception {
        int databaseSizeBeforeCreate = makimonoRepository.findAll().size();
        // Create the Makimono
        restMakimonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(makimono)))
            .andExpect(status().isCreated());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeCreate + 1);
        Makimono testMakimono = makimonoList.get(makimonoList.size() - 1);
        assertThat(testMakimono.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMakimono.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testMakimono.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testMakimono.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testMakimono.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testMakimono.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
        assertThat(testMakimono.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    void createMakimonoWithExistingId() throws Exception {
        // Create the Makimono with an existing ID
        makimono.setId(1L);

        int databaseSizeBeforeCreate = makimonoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMakimonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(makimono)))
            .andExpect(status().isBadRequest());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMakimonos() throws Exception {
        // Initialize the database
        makimonoRepository.saveAndFlush(makimono);

        // Get all the makimonoList
        restMakimonoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(makimono.getId().intValue())))
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
    void getMakimono() throws Exception {
        // Initialize the database
        makimonoRepository.saveAndFlush(makimono);

        // Get the makimono
        restMakimonoMockMvc
            .perform(get(ENTITY_API_URL_ID, makimono.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(makimono.getId().intValue()))
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
    void getNonExistingMakimono() throws Exception {
        // Get the makimono
        restMakimonoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMakimono() throws Exception {
        // Initialize the database
        makimonoRepository.saveAndFlush(makimono);

        int databaseSizeBeforeUpdate = makimonoRepository.findAll().size();

        // Update the makimono
        Makimono updatedMakimono = makimonoRepository.findById(makimono.getId()).get();
        // Disconnect from session so that the updates on updatedMakimono are not directly saved in db
        em.detach(updatedMakimono);
        updatedMakimono
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);

        restMakimonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMakimono.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMakimono))
            )
            .andExpect(status().isOk());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeUpdate);
        Makimono testMakimono = makimonoList.get(makimonoList.size() - 1);
        assertThat(testMakimono.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMakimono.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testMakimono.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testMakimono.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testMakimono.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testMakimono.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testMakimono.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void putNonExistingMakimono() throws Exception {
        int databaseSizeBeforeUpdate = makimonoRepository.findAll().size();
        makimono.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMakimonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, makimono.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(makimono))
            )
            .andExpect(status().isBadRequest());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMakimono() throws Exception {
        int databaseSizeBeforeUpdate = makimonoRepository.findAll().size();
        makimono.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMakimonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(makimono))
            )
            .andExpect(status().isBadRequest());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMakimono() throws Exception {
        int databaseSizeBeforeUpdate = makimonoRepository.findAll().size();
        makimono.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMakimonoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(makimono)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMakimonoWithPatch() throws Exception {
        // Initialize the database
        makimonoRepository.saveAndFlush(makimono);

        int databaseSizeBeforeUpdate = makimonoRepository.findAll().size();

        // Update the makimono using partial update
        Makimono partialUpdatedMakimono = new Makimono();
        partialUpdatedMakimono.setId(makimono.getId());

        partialUpdatedMakimono.promocao(UPDATED_PROMOCAO);

        restMakimonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMakimono.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMakimono))
            )
            .andExpect(status().isOk());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeUpdate);
        Makimono testMakimono = makimonoList.get(makimonoList.size() - 1);
        assertThat(testMakimono.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMakimono.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testMakimono.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testMakimono.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testMakimono.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testMakimono.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testMakimono.getAtivo()).isEqualTo(DEFAULT_ATIVO);
    }

    @Test
    @Transactional
    void fullUpdateMakimonoWithPatch() throws Exception {
        // Initialize the database
        makimonoRepository.saveAndFlush(makimono);

        int databaseSizeBeforeUpdate = makimonoRepository.findAll().size();

        // Update the makimono using partial update
        Makimono partialUpdatedMakimono = new Makimono();
        partialUpdatedMakimono.setId(makimono.getId());

        partialUpdatedMakimono
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO)
            .ativo(UPDATED_ATIVO);

        restMakimonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMakimono.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMakimono))
            )
            .andExpect(status().isOk());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeUpdate);
        Makimono testMakimono = makimonoList.get(makimonoList.size() - 1);
        assertThat(testMakimono.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMakimono.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testMakimono.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testMakimono.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testMakimono.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testMakimono.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testMakimono.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void patchNonExistingMakimono() throws Exception {
        int databaseSizeBeforeUpdate = makimonoRepository.findAll().size();
        makimono.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMakimonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, makimono.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(makimono))
            )
            .andExpect(status().isBadRequest());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMakimono() throws Exception {
        int databaseSizeBeforeUpdate = makimonoRepository.findAll().size();
        makimono.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMakimonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(makimono))
            )
            .andExpect(status().isBadRequest());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMakimono() throws Exception {
        int databaseSizeBeforeUpdate = makimonoRepository.findAll().size();
        makimono.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMakimonoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(makimono)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Makimono in the database
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMakimono() throws Exception {
        // Initialize the database
        makimonoRepository.saveAndFlush(makimono);

        int databaseSizeBeforeDelete = makimonoRepository.findAll().size();

        // Delete the makimono
        restMakimonoMockMvc
            .perform(delete(ENTITY_API_URL_ID, makimono.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Makimono> makimonoList = makimonoRepository.findAll();
        assertThat(makimonoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
