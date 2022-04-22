package com.maiworm.sushi.web.rest;

import com.maiworm.sushi.domain.Uramaki;
import com.maiworm.sushi.repository.UramakiRepository;
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
 * REST controller for managing {@link com.maiworm.sushi.domain.Uramaki}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UramakiResource {

    private final Logger log = LoggerFactory.getLogger(UramakiResource.class);

    private static final String ENTITY_NAME = "uramaki";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UramakiRepository uramakiRepository;

    public UramakiResource(UramakiRepository uramakiRepository) {
        this.uramakiRepository = uramakiRepository;
    }

    /**
     * {@code POST  /uramakis} : Create a new uramaki.
     *
     * @param uramaki the uramaki to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uramaki, or with status {@code 400 (Bad Request)} if the uramaki has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/uramakis")
    public ResponseEntity<Uramaki> createUramaki(@RequestBody Uramaki uramaki) throws URISyntaxException {
        log.debug("REST request to save Uramaki : {}", uramaki);
        if (uramaki.getId() != null) {
            throw new BadRequestAlertException("A new uramaki cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Uramaki result = uramakiRepository.save(uramaki);
        return ResponseEntity
            .created(new URI("/api/uramakis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /uramakis/:id} : Updates an existing uramaki.
     *
     * @param id the id of the uramaki to save.
     * @param uramaki the uramaki to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uramaki,
     * or with status {@code 400 (Bad Request)} if the uramaki is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uramaki couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uramakis/{id}")
    public ResponseEntity<Uramaki> updateUramaki(@PathVariable(value = "id", required = false) final Long id, @RequestBody Uramaki uramaki)
        throws URISyntaxException {
        log.debug("REST request to update Uramaki : {}, {}", id, uramaki);
        if (uramaki.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uramaki.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uramakiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Uramaki result = uramakiRepository.save(uramaki);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, uramaki.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /uramakis/:id} : Partial updates given fields of an existing uramaki, field will ignore if it is null
     *
     * @param id the id of the uramaki to save.
     * @param uramaki the uramaki to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uramaki,
     * or with status {@code 400 (Bad Request)} if the uramaki is not valid,
     * or with status {@code 404 (Not Found)} if the uramaki is not found,
     * or with status {@code 500 (Internal Server Error)} if the uramaki couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/uramakis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Uramaki> partialUpdateUramaki(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Uramaki uramaki
    ) throws URISyntaxException {
        log.debug("REST request to partial update Uramaki partially : {}, {}", id, uramaki);
        if (uramaki.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uramaki.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uramakiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Uramaki> result = uramakiRepository
            .findById(uramaki.getId())
            .map(existingUramaki -> {
                if (uramaki.getNome() != null) {
                    existingUramaki.setNome(uramaki.getNome());
                }
                if (uramaki.getDescricao() != null) {
                    existingUramaki.setDescricao(uramaki.getDescricao());
                }
                if (uramaki.getImagem() != null) {
                    existingUramaki.setImagem(uramaki.getImagem());
                }
                if (uramaki.getImagemContentType() != null) {
                    existingUramaki.setImagemContentType(uramaki.getImagemContentType());
                }
                if (uramaki.getPreco() != null) {
                    existingUramaki.setPreco(uramaki.getPreco());
                }
                if (uramaki.getPromocao() != null) {
                    existingUramaki.setPromocao(uramaki.getPromocao());
                }

                return existingUramaki;
            })
            .map(uramakiRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, uramaki.getId().toString())
        );
    }

    /**
     * {@code GET  /uramakis} : get all the uramakis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uramakis in body.
     */
    @GetMapping("/uramakis")
    public List<Uramaki> getAllUramakis() {
        log.debug("REST request to get all Uramakis");
        return uramakiRepository.findAll();
    }

    /**
     * {@code GET  /uramakis/:id} : get the "id" uramaki.
     *
     * @param id the id of the uramaki to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uramaki, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uramakis/{id}")
    public ResponseEntity<Uramaki> getUramaki(@PathVariable Long id) {
        log.debug("REST request to get Uramaki : {}", id);
        Optional<Uramaki> uramaki = uramakiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(uramaki);
    }

    /**
     * {@code DELETE  /uramakis/:id} : delete the "id" uramaki.
     *
     * @param id the id of the uramaki to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/uramakis/{id}")
    public ResponseEntity<Void> deleteUramaki(@PathVariable Long id) {
        log.debug("REST request to delete Uramaki : {}", id);
        uramakiRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
