package com.maiworm.sushi.web.rest;

import com.maiworm.sushi.domain.Especiais;
import com.maiworm.sushi.repository.EspeciaisRepository;
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
 * REST controller for managing {@link com.maiworm.sushi.domain.Especiais}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EspeciaisResource {

    private final Logger log = LoggerFactory.getLogger(EspeciaisResource.class);

    private static final String ENTITY_NAME = "especiais";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EspeciaisRepository especiaisRepository;

    public EspeciaisResource(EspeciaisRepository especiaisRepository) {
        this.especiaisRepository = especiaisRepository;
    }

    /**
     * {@code POST  /especiais} : Create a new especiais.
     *
     * @param especiais the especiais to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new especiais, or with status {@code 400 (Bad Request)} if the especiais has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/especiais")
    public ResponseEntity<Especiais> createEspeciais(@RequestBody Especiais especiais) throws URISyntaxException {
        log.debug("REST request to save Especiais : {}", especiais);
        if (especiais.getId() != null) {
            throw new BadRequestAlertException("A new especiais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Especiais result = especiaisRepository.save(especiais);
        return ResponseEntity
            .created(new URI("/api/especiais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /especiais/:id} : Updates an existing especiais.
     *
     * @param id the id of the especiais to save.
     * @param especiais the especiais to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especiais,
     * or with status {@code 400 (Bad Request)} if the especiais is not valid,
     * or with status {@code 500 (Internal Server Error)} if the especiais couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/especiais/{id}")
    public ResponseEntity<Especiais> updateEspeciais(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Especiais especiais
    ) throws URISyntaxException {
        log.debug("REST request to update Especiais : {}, {}", id, especiais);
        if (especiais.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, especiais.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!especiaisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Especiais result = especiaisRepository.save(especiais);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, especiais.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /especiais/:id} : Partial updates given fields of an existing especiais, field will ignore if it is null
     *
     * @param id the id of the especiais to save.
     * @param especiais the especiais to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especiais,
     * or with status {@code 400 (Bad Request)} if the especiais is not valid,
     * or with status {@code 404 (Not Found)} if the especiais is not found,
     * or with status {@code 500 (Internal Server Error)} if the especiais couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/especiais/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Especiais> partialUpdateEspeciais(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Especiais especiais
    ) throws URISyntaxException {
        log.debug("REST request to partial update Especiais partially : {}, {}", id, especiais);
        if (especiais.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, especiais.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!especiaisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Especiais> result = especiaisRepository
            .findById(especiais.getId())
            .map(existingEspeciais -> {
                if (especiais.getNome() != null) {
                    existingEspeciais.setNome(especiais.getNome());
                }
                if (especiais.getDescricao() != null) {
                    existingEspeciais.setDescricao(especiais.getDescricao());
                }
                if (especiais.getImagem() != null) {
                    existingEspeciais.setImagem(especiais.getImagem());
                }
                if (especiais.getImagemContentType() != null) {
                    existingEspeciais.setImagemContentType(especiais.getImagemContentType());
                }
                if (especiais.getPreco() != null) {
                    existingEspeciais.setPreco(especiais.getPreco());
                }
                if (especiais.getPromocao() != null) {
                    existingEspeciais.setPromocao(especiais.getPromocao());
                }
                if (especiais.getAtivo() != null) {
                    existingEspeciais.setAtivo(especiais.getAtivo());
                }

                return existingEspeciais;
            })
            .map(especiaisRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, especiais.getId().toString())
        );
    }

    /**
     * {@code GET  /especiais} : get all the especiais.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of especiais in body.
     */
    @GetMapping("/especiais")
    public List<Especiais> getAllEspeciais() {
        log.debug("REST request to get all Especiais");
        return especiaisRepository.findAll();
    }

    /**
     * {@code GET  /especiais/:id} : get the "id" especiais.
     *
     * @param id the id of the especiais to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the especiais, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/especiais/{id}")
    public ResponseEntity<Especiais> getEspeciais(@PathVariable Long id) {
        log.debug("REST request to get Especiais : {}", id);
        Optional<Especiais> especiais = especiaisRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(especiais);
    }

    /**
     * {@code DELETE  /especiais/:id} : delete the "id" especiais.
     *
     * @param id the id of the especiais to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/especiais/{id}")
    public ResponseEntity<Void> deleteEspeciais(@PathVariable Long id) {
        log.debug("REST request to delete Especiais : {}", id);
        especiaisRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
