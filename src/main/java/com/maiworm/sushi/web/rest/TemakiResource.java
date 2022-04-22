package com.maiworm.sushi.web.rest;

import com.maiworm.sushi.domain.Temaki;
import com.maiworm.sushi.repository.TemakiRepository;
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
 * REST controller for managing {@link com.maiworm.sushi.domain.Temaki}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TemakiResource {

    private final Logger log = LoggerFactory.getLogger(TemakiResource.class);

    private static final String ENTITY_NAME = "temaki";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemakiRepository temakiRepository;

    public TemakiResource(TemakiRepository temakiRepository) {
        this.temakiRepository = temakiRepository;
    }

    /**
     * {@code POST  /temakis} : Create a new temaki.
     *
     * @param temaki the temaki to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new temaki, or with status {@code 400 (Bad Request)} if the temaki has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/temakis")
    public ResponseEntity<Temaki> createTemaki(@RequestBody Temaki temaki) throws URISyntaxException {
        log.debug("REST request to save Temaki : {}", temaki);
        if (temaki.getId() != null) {
            throw new BadRequestAlertException("A new temaki cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Temaki result = temakiRepository.save(temaki);
        return ResponseEntity
            .created(new URI("/api/temakis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /temakis/:id} : Updates an existing temaki.
     *
     * @param id the id of the temaki to save.
     * @param temaki the temaki to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temaki,
     * or with status {@code 400 (Bad Request)} if the temaki is not valid,
     * or with status {@code 500 (Internal Server Error)} if the temaki couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/temakis/{id}")
    public ResponseEntity<Temaki> updateTemaki(@PathVariable(value = "id", required = false) final Long id, @RequestBody Temaki temaki)
        throws URISyntaxException {
        log.debug("REST request to update Temaki : {}, {}", id, temaki);
        if (temaki.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, temaki.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!temakiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Temaki result = temakiRepository.save(temaki);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, temaki.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /temakis/:id} : Partial updates given fields of an existing temaki, field will ignore if it is null
     *
     * @param id the id of the temaki to save.
     * @param temaki the temaki to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temaki,
     * or with status {@code 400 (Bad Request)} if the temaki is not valid,
     * or with status {@code 404 (Not Found)} if the temaki is not found,
     * or with status {@code 500 (Internal Server Error)} if the temaki couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/temakis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Temaki> partialUpdateTemaki(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Temaki temaki
    ) throws URISyntaxException {
        log.debug("REST request to partial update Temaki partially : {}, {}", id, temaki);
        if (temaki.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, temaki.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!temakiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Temaki> result = temakiRepository
            .findById(temaki.getId())
            .map(existingTemaki -> {
                if (temaki.getNome() != null) {
                    existingTemaki.setNome(temaki.getNome());
                }
                if (temaki.getDescricao() != null) {
                    existingTemaki.setDescricao(temaki.getDescricao());
                }
                if (temaki.getImagem() != null) {
                    existingTemaki.setImagem(temaki.getImagem());
                }
                if (temaki.getImagemContentType() != null) {
                    existingTemaki.setImagemContentType(temaki.getImagemContentType());
                }
                if (temaki.getPreco() != null) {
                    existingTemaki.setPreco(temaki.getPreco());
                }
                if (temaki.getPromocao() != null) {
                    existingTemaki.setPromocao(temaki.getPromocao());
                }
                if (temaki.getAtivo() != null) {
                    existingTemaki.setAtivo(temaki.getAtivo());
                }

                return existingTemaki;
            })
            .map(temakiRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, temaki.getId().toString())
        );
    }

    /**
     * {@code GET  /temakis} : get all the temakis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of temakis in body.
     */
    @GetMapping("/temakis")
    public List<Temaki> getAllTemakis() {
        log.debug("REST request to get all Temakis");
        return temakiRepository.findAll();
    }

    /**
     * {@code GET  /temakis/:id} : get the "id" temaki.
     *
     * @param id the id of the temaki to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the temaki, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/temakis/{id}")
    public ResponseEntity<Temaki> getTemaki(@PathVariable Long id) {
        log.debug("REST request to get Temaki : {}", id);
        Optional<Temaki> temaki = temakiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(temaki);
    }

    /**
     * {@code DELETE  /temakis/:id} : delete the "id" temaki.
     *
     * @param id the id of the temaki to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/temakis/{id}")
    public ResponseEntity<Void> deleteTemaki(@PathVariable Long id) {
        log.debug("REST request to delete Temaki : {}", id);
        temakiRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
