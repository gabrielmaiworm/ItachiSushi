package com.maiworm.sushi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.maiworm.sushi.IntegrationTest;
import com.maiworm.sushi.domain.Hot;
import com.maiworm.sushi.repository.HotRepository;
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
 * Integration tests for the {@link HotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HotResourceIT {

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

    private static final String ENTITY_API_URL = "/api/hots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HotRepository hotRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHotMockMvc;

    private Hot hot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hot createEntity(EntityManager em) {
        Hot hot = new Hot()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .imagem(DEFAULT_IMAGEM)
            .imagemContentType(DEFAULT_IMAGEM_CONTENT_TYPE)
            .preco(DEFAULT_PRECO)
            .promocao(DEFAULT_PROMOCAO);
        return hot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hot createUpdatedEntity(EntityManager em) {
        Hot hot = new Hot()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);
        return hot;
    }

    @BeforeEach
    public void initTest() {
        hot = createEntity(em);
    }

    @Test
    @Transactional
    void createHot() throws Exception {
        int databaseSizeBeforeCreate = hotRepository.findAll().size();
        // Create the Hot
        restHotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hot)))
            .andExpect(status().isCreated());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeCreate + 1);
        Hot testHot = hotList.get(hotList.size() - 1);
        assertThat(testHot.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testHot.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testHot.getImagem()).isEqualTo(DEFAULT_IMAGEM);
        assertThat(testHot.getImagemContentType()).isEqualTo(DEFAULT_IMAGEM_CONTENT_TYPE);
        assertThat(testHot.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testHot.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
    }

    @Test
    @Transactional
    void createHotWithExistingId() throws Exception {
        // Create the Hot with an existing ID
        hot.setId(1L);

        int databaseSizeBeforeCreate = hotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hot)))
            .andExpect(status().isBadRequest());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHots() throws Exception {
        // Initialize the database
        hotRepository.saveAndFlush(hot);

        // Get all the hotList
        restHotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hot.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].imagemContentType").value(hasItem(DEFAULT_IMAGEM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imagem").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEM))))
            .andExpect(jsonPath("$.[*].preco").value(hasItem(DEFAULT_PRECO.doubleValue())))
            .andExpect(jsonPath("$.[*].promocao").value(hasItem(DEFAULT_PROMOCAO.booleanValue())));
    }

    @Test
    @Transactional
    void getHot() throws Exception {
        // Initialize the database
        hotRepository.saveAndFlush(hot);

        // Get the hot
        restHotMockMvc
            .perform(get(ENTITY_API_URL_ID, hot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hot.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.imagemContentType").value(DEFAULT_IMAGEM_CONTENT_TYPE))
            .andExpect(jsonPath("$.imagem").value(Base64Utils.encodeToString(DEFAULT_IMAGEM)))
            .andExpect(jsonPath("$.preco").value(DEFAULT_PRECO.doubleValue()))
            .andExpect(jsonPath("$.promocao").value(DEFAULT_PROMOCAO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHot() throws Exception {
        // Get the hot
        restHotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHot() throws Exception {
        // Initialize the database
        hotRepository.saveAndFlush(hot);

        int databaseSizeBeforeUpdate = hotRepository.findAll().size();

        // Update the hot
        Hot updatedHot = hotRepository.findById(hot.getId()).get();
        // Disconnect from session so that the updates on updatedHot are not directly saved in db
        em.detach(updatedHot);
        updatedHot
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restHotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHot))
            )
            .andExpect(status().isOk());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate);
        Hot testHot = hotList.get(hotList.size() - 1);
        assertThat(testHot.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testHot.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testHot.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testHot.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testHot.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testHot.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void putNonExistingHot() throws Exception {
        int databaseSizeBeforeUpdate = hotRepository.findAll().size();
        hot.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hot.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHot() throws Exception {
        int databaseSizeBeforeUpdate = hotRepository.findAll().size();
        hot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHot() throws Exception {
        int databaseSizeBeforeUpdate = hotRepository.findAll().size();
        hot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHotWithPatch() throws Exception {
        // Initialize the database
        hotRepository.saveAndFlush(hot);

        int databaseSizeBeforeUpdate = hotRepository.findAll().size();

        // Update the hot using partial update
        Hot partialUpdatedHot = new Hot();
        partialUpdatedHot.setId(hot.getId());

        partialUpdatedHot
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE);

        restHotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHot))
            )
            .andExpect(status().isOk());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate);
        Hot testHot = hotList.get(hotList.size() - 1);
        assertThat(testHot.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testHot.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testHot.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testHot.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testHot.getPreco()).isEqualTo(DEFAULT_PRECO);
        assertThat(testHot.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
    }

    @Test
    @Transactional
    void fullUpdateHotWithPatch() throws Exception {
        // Initialize the database
        hotRepository.saveAndFlush(hot);

        int databaseSizeBeforeUpdate = hotRepository.findAll().size();

        // Update the hot using partial update
        Hot partialUpdatedHot = new Hot();
        partialUpdatedHot.setId(hot.getId());

        partialUpdatedHot
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .imagem(UPDATED_IMAGEM)
            .imagemContentType(UPDATED_IMAGEM_CONTENT_TYPE)
            .preco(UPDATED_PRECO)
            .promocao(UPDATED_PROMOCAO);

        restHotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHot))
            )
            .andExpect(status().isOk());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate);
        Hot testHot = hotList.get(hotList.size() - 1);
        assertThat(testHot.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testHot.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testHot.getImagem()).isEqualTo(UPDATED_IMAGEM);
        assertThat(testHot.getImagemContentType()).isEqualTo(UPDATED_IMAGEM_CONTENT_TYPE);
        assertThat(testHot.getPreco()).isEqualTo(UPDATED_PRECO);
        assertThat(testHot.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
    }

    @Test
    @Transactional
    void patchNonExistingHot() throws Exception {
        int databaseSizeBeforeUpdate = hotRepository.findAll().size();
        hot.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHot() throws Exception {
        int databaseSizeBeforeUpdate = hotRepository.findAll().size();
        hot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHot() throws Exception {
        int databaseSizeBeforeUpdate = hotRepository.findAll().size();
        hot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hot in the database
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHot() throws Exception {
        // Initialize the database
        hotRepository.saveAndFlush(hot);

        int databaseSizeBeforeDelete = hotRepository.findAll().size();

        // Delete the hot
        restHotMockMvc.perform(delete(ENTITY_API_URL_ID, hot.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hot> hotList = hotRepository.findAll();
        assertThat(hotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
