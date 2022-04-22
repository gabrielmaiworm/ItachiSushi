package com.maiworm.sushi.web.rest;

import com.maiworm.sushi.domain.Yakisoba;
import com.maiworm.sushi.repository.YakisobaRepository;
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
 * REST controller for managing {@link com.maiworm.sushi.domain.Yakisoba}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class YakisobaResource {

    private final Logger log = LoggerFactory.getLogger(YakisobaResource.class);

    private static final String ENTITY_NAME = "yakisoba";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final YakisobaRepository yakisobaRepository;

    public YakisobaResource(YakisobaRepository yakisobaRepository) {
        this.yakisobaRepository = yakisobaRepository;
    }

    /**
     * {@code POST  /yakisobas} : Create a new yakisoba.
     *
     * @param yakisoba the yakisoba to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new yakisoba, or with status {@code 400 (Bad Request)} if the yakisoba has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/yakisobas")
    public ResponseEntity<Yakisoba> createYakisoba(@RequestBody Yakisoba yakisoba) throws URISyntaxException {
        log.debug("REST request to save Yakisoba : {}", yakisoba);
        if (yakisoba.getId() != null) {
            throw new BadRequestAlertException("A new yakisoba cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Yakisoba result = yakisobaRepository.save(yakisoba);
        return ResponseEntity
            .created(new URI("/api/yakisobas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /yakisobas/:id} : Updates an existing yakisoba.
     *
     * @param id the id of the yakisoba to save.
     * @param yakisoba the yakisoba to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated yakisoba,
     * or with status {@code 400 (Bad Request)} if the yakisoba is not valid,
     * or with status {@code 500 (Internal Server Error)} if the yakisoba couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/yakisobas/{id}")
    public ResponseEntity<Yakisoba> updateYakisoba(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Yakisoba yakisoba
    ) throws URISyntaxException {
        log.debug("REST request to update Yakisoba : {}, {}", id, yakisoba);
        if (yakisoba.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, yakisoba.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!yakisobaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Yakisoba result = yakisobaRepository.save(yakisoba);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, yakisoba.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /yakisobas/:id} : Partial updates given fields of an existing yakisoba, field will ignore if it is null
     *
     * @param id the id of the yakisoba to save.
     * @param yakisoba the yakisoba to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated yakisoba,
     * or with status {@code 400 (Bad Request)} if the yakisoba is not valid,
     * or with status {@code 404 (Not Found)} if the yakisoba is not found,
     * or with status {@code 500 (Internal Server Error)} if the yakisoba couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/yakisobas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Yakisoba> partialUpdateYakisoba(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Yakisoba yakisoba
    ) throws URISyntaxException {
        log.debug("REST request to partial update Yakisoba partially : {}, {}", id, yakisoba);
        if (yakisoba.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, yakisoba.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!yakisobaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Yakisoba> result = yakisobaRepository
            .findById(yakisoba.getId())
            .map(existingYakisoba -> {
                if (yakisoba.getNome() != null) {
                    existingYakisoba.setNome(yakisoba.getNome());
                }
                if (yakisoba.getDescricao() != null) {
                    existingYakisoba.setDescricao(yakisoba.getDescricao());
                }
                if (yakisoba.getImagem() != null) {
                    existingYakisoba.setImagem(yakisoba.getImagem());
                }
                if (yakisoba.getImagemContentType() != null) {
                    existingYakisoba.setImagemContentType(yakisoba.getImagemContentType());
                }
                if (yakisoba.getPreco() != null) {
                    existingYakisoba.setPreco(yakisoba.getPreco());
                }
                if (yakisoba.getPromocao() != null) {
                    existingYakisoba.setPromocao(yakisoba.getPromocao());
                }
                if (yakisoba.getAtivo() != null) {
                    existingYakisoba.setAtivo(yakisoba.getAtivo());
                }

                return existingYakisoba;
            })
            .map(yakisobaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, yakisoba.getId().toString())
        );
    }

    /**
     * {@code GET  /yakisobas} : get all the yakisobas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of yakisobas in body.
     */
    @GetMapping("/yakisobas")
    public List<Yakisoba> getAllYakisobas() {
        log.debug("REST request to get all Yakisobas");
        return yakisobaRepository.findAll();
    }

    /**
     * {@code GET  /yakisobas/:id} : get the "id" yakisoba.
     *
     * @param id the id of the yakisoba to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the yakisoba, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/yakisobas/{id}")
    public ResponseEntity<Yakisoba> getYakisoba(@PathVariable Long id) {
        log.debug("REST request to get Yakisoba : {}", id);
        Optional<Yakisoba> yakisoba = yakisobaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(yakisoba);
    }

    /**
     * {@code DELETE  /yakisobas/:id} : delete the "id" yakisoba.
     *
     * @param id the id of the yakisoba to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/yakisobas/{id}")
    public ResponseEntity<Void> deleteYakisoba(@PathVariable Long id) {
        log.debug("REST request to delete Yakisoba : {}", id);
        yakisobaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
