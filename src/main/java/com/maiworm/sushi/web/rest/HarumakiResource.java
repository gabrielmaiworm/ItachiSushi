package com.maiworm.sushi.web.rest;

import com.maiworm.sushi.domain.Harumaki;
import com.maiworm.sushi.repository.HarumakiRepository;
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
 * REST controller for managing {@link com.maiworm.sushi.domain.Harumaki}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HarumakiResource {

    private final Logger log = LoggerFactory.getLogger(HarumakiResource.class);

    private static final String ENTITY_NAME = "harumaki";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HarumakiRepository harumakiRepository;

    public HarumakiResource(HarumakiRepository harumakiRepository) {
        this.harumakiRepository = harumakiRepository;
    }

    /**
     * {@code POST  /harumakis} : Create a new harumaki.
     *
     * @param harumaki the harumaki to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new harumaki, or with status {@code 400 (Bad Request)} if the harumaki has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/harumakis")
    public ResponseEntity<Harumaki> createHarumaki(@RequestBody Harumaki harumaki) throws URISyntaxException {
        log.debug("REST request to save Harumaki : {}", harumaki);
        if (harumaki.getId() != null) {
            throw new BadRequestAlertException("A new harumaki cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Harumaki result = harumakiRepository.save(harumaki);
        return ResponseEntity
            .created(new URI("/api/harumakis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /harumakis/:id} : Updates an existing harumaki.
     *
     * @param id the id of the harumaki to save.
     * @param harumaki the harumaki to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated harumaki,
     * or with status {@code 400 (Bad Request)} if the harumaki is not valid,
     * or with status {@code 500 (Internal Server Error)} if the harumaki couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/harumakis/{id}")
    public ResponseEntity<Harumaki> updateHarumaki(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Harumaki harumaki
    ) throws URISyntaxException {
        log.debug("REST request to update Harumaki : {}, {}", id, harumaki);
        if (harumaki.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, harumaki.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!harumakiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Harumaki result = harumakiRepository.save(harumaki);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, harumaki.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /harumakis/:id} : Partial updates given fields of an existing harumaki, field will ignore if it is null
     *
     * @param id the id of the harumaki to save.
     * @param harumaki the harumaki to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated harumaki,
     * or with status {@code 400 (Bad Request)} if the harumaki is not valid,
     * or with status {@code 404 (Not Found)} if the harumaki is not found,
     * or with status {@code 500 (Internal Server Error)} if the harumaki couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/harumakis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Harumaki> partialUpdateHarumaki(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Harumaki harumaki
    ) throws URISyntaxException {
        log.debug("REST request to partial update Harumaki partially : {}, {}", id, harumaki);
        if (harumaki.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, harumaki.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!harumakiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Harumaki> result = harumakiRepository
            .findById(harumaki.getId())
            .map(existingHarumaki -> {
                if (harumaki.getNome() != null) {
                    existingHarumaki.setNome(harumaki.getNome());
                }
                if (harumaki.getDescricao() != null) {
                    existingHarumaki.setDescricao(harumaki.getDescricao());
                }
                if (harumaki.getImagem() != null) {
                    existingHarumaki.setImagem(harumaki.getImagem());
                }
                if (harumaki.getImagemContentType() != null) {
                    existingHarumaki.setImagemContentType(harumaki.getImagemContentType());
                }
                if (harumaki.getPreco() != null) {
                    existingHarumaki.setPreco(harumaki.getPreco());
                }
                if (harumaki.getPromocao() != null) {
                    existingHarumaki.setPromocao(harumaki.getPromocao());
                }
                if (harumaki.getAtivo() != null) {
                    existingHarumaki.setAtivo(harumaki.getAtivo());
                }

                return existingHarumaki;
            })
            .map(harumakiRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, harumaki.getId().toString())
        );
    }

    /**
     * {@code GET  /harumakis} : get all the harumakis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of harumakis in body.
     */
    @GetMapping("/harumakis")
    public List<Harumaki> getAllHarumakis() {
        log.debug("REST request to get all Harumakis");
        return harumakiRepository.findAll();
    }

    /**
     * {@code GET  /harumakis/:id} : get the "id" harumaki.
     *
     * @param id the id of the harumaki to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the harumaki, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/harumakis/{id}")
    public ResponseEntity<Harumaki> getHarumaki(@PathVariable Long id) {
        log.debug("REST request to get Harumaki : {}", id);
        Optional<Harumaki> harumaki = harumakiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(harumaki);
    }

    /**
     * {@code DELETE  /harumakis/:id} : delete the "id" harumaki.
     *
     * @param id the id of the harumaki to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/harumakis/{id}")
    public ResponseEntity<Void> deleteHarumaki(@PathVariable Long id) {
        log.debug("REST request to delete Harumaki : {}", id);
        harumakiRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
