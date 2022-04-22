package com.maiworm.sushi.web.rest;

import com.maiworm.sushi.domain.Makimono;
import com.maiworm.sushi.repository.MakimonoRepository;
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
 * REST controller for managing {@link com.maiworm.sushi.domain.Makimono}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MakimonoResource {

    private final Logger log = LoggerFactory.getLogger(MakimonoResource.class);

    private static final String ENTITY_NAME = "makimono";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MakimonoRepository makimonoRepository;

    public MakimonoResource(MakimonoRepository makimonoRepository) {
        this.makimonoRepository = makimonoRepository;
    }

    /**
     * {@code POST  /makimonos} : Create a new makimono.
     *
     * @param makimono the makimono to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new makimono, or with status {@code 400 (Bad Request)} if the makimono has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/makimonos")
    public ResponseEntity<Makimono> createMakimono(@RequestBody Makimono makimono) throws URISyntaxException {
        log.debug("REST request to save Makimono : {}", makimono);
        if (makimono.getId() != null) {
            throw new BadRequestAlertException("A new makimono cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Makimono result = makimonoRepository.save(makimono);
        return ResponseEntity
            .created(new URI("/api/makimonos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /makimonos/:id} : Updates an existing makimono.
     *
     * @param id the id of the makimono to save.
     * @param makimono the makimono to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated makimono,
     * or with status {@code 400 (Bad Request)} if the makimono is not valid,
     * or with status {@code 500 (Internal Server Error)} if the makimono couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/makimonos/{id}")
    public ResponseEntity<Makimono> updateMakimono(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Makimono makimono
    ) throws URISyntaxException {
        log.debug("REST request to update Makimono : {}, {}", id, makimono);
        if (makimono.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, makimono.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!makimonoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Makimono result = makimonoRepository.save(makimono);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, makimono.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /makimonos/:id} : Partial updates given fields of an existing makimono, field will ignore if it is null
     *
     * @param id the id of the makimono to save.
     * @param makimono the makimono to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated makimono,
     * or with status {@code 400 (Bad Request)} if the makimono is not valid,
     * or with status {@code 404 (Not Found)} if the makimono is not found,
     * or with status {@code 500 (Internal Server Error)} if the makimono couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/makimonos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Makimono> partialUpdateMakimono(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Makimono makimono
    ) throws URISyntaxException {
        log.debug("REST request to partial update Makimono partially : {}, {}", id, makimono);
        if (makimono.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, makimono.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!makimonoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Makimono> result = makimonoRepository
            .findById(makimono.getId())
            .map(existingMakimono -> {
                if (makimono.getNome() != null) {
                    existingMakimono.setNome(makimono.getNome());
                }
                if (makimono.getDescricao() != null) {
                    existingMakimono.setDescricao(makimono.getDescricao());
                }
                if (makimono.getImagem() != null) {
                    existingMakimono.setImagem(makimono.getImagem());
                }
                if (makimono.getImagemContentType() != null) {
                    existingMakimono.setImagemContentType(makimono.getImagemContentType());
                }
                if (makimono.getPreco() != null) {
                    existingMakimono.setPreco(makimono.getPreco());
                }
                if (makimono.getPromocao() != null) {
                    existingMakimono.setPromocao(makimono.getPromocao());
                }
                if (makimono.getAtivo() != null) {
                    existingMakimono.setAtivo(makimono.getAtivo());
                }

                return existingMakimono;
            })
            .map(makimonoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, makimono.getId().toString())
        );
    }

    /**
     * {@code GET  /makimonos} : get all the makimonos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of makimonos in body.
     */
    @GetMapping("/makimonos")
    public List<Makimono> getAllMakimonos() {
        log.debug("REST request to get all Makimonos");
        return makimonoRepository.findAll();
    }

    /**
     * {@code GET  /makimonos/:id} : get the "id" makimono.
     *
     * @param id the id of the makimono to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the makimono, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/makimonos/{id}")
    public ResponseEntity<Makimono> getMakimono(@PathVariable Long id) {
        log.debug("REST request to get Makimono : {}", id);
        Optional<Makimono> makimono = makimonoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(makimono);
    }

    /**
     * {@code DELETE  /makimonos/:id} : delete the "id" makimono.
     *
     * @param id the id of the makimono to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/makimonos/{id}")
    public ResponseEntity<Void> deleteMakimono(@PathVariable Long id) {
        log.debug("REST request to delete Makimono : {}", id);
        makimonoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
