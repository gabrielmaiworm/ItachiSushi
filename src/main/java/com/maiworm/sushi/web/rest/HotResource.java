package com.maiworm.sushi.web.rest;

import com.maiworm.sushi.domain.Hot;
import com.maiworm.sushi.repository.HotRepository;
import com.maiworm.sushi.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.maiworm.sushi.domain.Hot}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HotResource {

    private final Logger log = LoggerFactory.getLogger(HotResource.class);

    private static final String ENTITY_NAME = "hot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HotRepository hotRepository;

    public HotResource(HotRepository hotRepository) {
        this.hotRepository = hotRepository;
    }

    /**
     * {@code POST  /hots} : Create a new hot.
     *
     * @param hot the hot to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hot, or with status {@code 400 (Bad Request)} if the hot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hots")
    public ResponseEntity<Hot> createHot(@RequestBody Hot hot) throws URISyntaxException {
        log.debug("REST request to save Hot : {}", hot);
        if (hot.getId() != null) {
            throw new BadRequestAlertException("A new hot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hot result = hotRepository.save(hot);
        return ResponseEntity
            .created(new URI("/api/hots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hots/:id} : Updates an existing hot.
     *
     * @param id the id of the hot to save.
     * @param hot the hot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hot,
     * or with status {@code 400 (Bad Request)} if the hot is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hots/{id}")
    public ResponseEntity<Hot> updateHot(@PathVariable(value = "id", required = false) final Long id, @RequestBody Hot hot)
        throws URISyntaxException {
        log.debug("REST request to update Hot : {}, {}", id, hot);
        if (hot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Hot result = hotRepository.save(hot);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hot.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hots/:id} : Partial updates given fields of an existing hot, field will ignore if it is null
     *
     * @param id the id of the hot to save.
     * @param hot the hot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hot,
     * or with status {@code 400 (Bad Request)} if the hot is not valid,
     * or with status {@code 404 (Not Found)} if the hot is not found,
     * or with status {@code 500 (Internal Server Error)} if the hot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hots/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Hot> partialUpdateHot(@PathVariable(value = "id", required = false) final Long id, @RequestBody Hot hot)
        throws URISyntaxException {
        log.debug("REST request to partial update Hot partially : {}, {}", id, hot);
        if (hot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Hot> result = hotRepository
            .findById(hot.getId())
            .map(existingHot -> {
                if (hot.getNome() != null) {
                    existingHot.setNome(hot.getNome());
                }
                if (hot.getDescricao() != null) {
                    existingHot.setDescricao(hot.getDescricao());
                }
                if (hot.getImagem() != null) {
                    existingHot.setImagem(hot.getImagem());
                }
                if (hot.getImagemContentType() != null) {
                    existingHot.setImagemContentType(hot.getImagemContentType());
                }
                if (hot.getPreco() != null) {
                    existingHot.setPreco(hot.getPreco());
                }
                if (hot.getPromocao() != null) {
                    existingHot.setPromocao(hot.getPromocao());
                }

                return existingHot;
            })
            .map(hotRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hot.getId().toString())
        );
    }

    /**
     * {@code GET  /hots} : get all the hots.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hots in body.
     */
    @GetMapping("/hots")
    public List<Hot> getAllHots() {
        log.debug("REST request to get all Hots");
        return hotRepository.findAll();
    }

    /**
     * {@code GET  /hots/:id} : get the "id" hot.
     *
     * @param id the id of the hot to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hot, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hots/{id}")
    public ResponseEntity<Hot> getHot(@PathVariable Long id) {
        log.debug("REST request to get Hot : {}", id);
        Optional<Hot> hot = hotRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hot);
    }

    /**
     * {@code DELETE  /hots/:id} : delete the "id" hot.
     *
     * @param id the id of the hot to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hots/{id}")
    public ResponseEntity<Void> deleteHot(@PathVariable Long id) {
        log.debug("REST request to delete Hot : {}", id);
        hotRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
