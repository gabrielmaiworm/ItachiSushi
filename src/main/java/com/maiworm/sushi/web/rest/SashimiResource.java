package com.maiworm.sushi.web.rest;

import com.maiworm.sushi.domain.Sashimi;
import com.maiworm.sushi.repository.SashimiRepository;
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
 * REST controller for managing {@link com.maiworm.sushi.domain.Sashimi}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SashimiResource {

    private final Logger log = LoggerFactory.getLogger(SashimiResource.class);

    private static final String ENTITY_NAME = "sashimi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SashimiRepository sashimiRepository;

    public SashimiResource(SashimiRepository sashimiRepository) {
        this.sashimiRepository = sashimiRepository;
    }

    /**
     * {@code POST  /sashimis} : Create a new sashimi.
     *
     * @param sashimi the sashimi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sashimi, or with status {@code 400 (Bad Request)} if the sashimi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sashimis")
    public ResponseEntity<Sashimi> createSashimi(@RequestBody Sashimi sashimi) throws URISyntaxException {
        log.debug("REST request to save Sashimi : {}", sashimi);
        if (sashimi.getId() != null) {
            throw new BadRequestAlertException("A new sashimi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sashimi result = sashimiRepository.save(sashimi);
        return ResponseEntity
            .created(new URI("/api/sashimis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sashimis/:id} : Updates an existing sashimi.
     *
     * @param id the id of the sashimi to save.
     * @param sashimi the sashimi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sashimi,
     * or with status {@code 400 (Bad Request)} if the sashimi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sashimi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sashimis/{id}")
    public ResponseEntity<Sashimi> updateSashimi(@PathVariable(value = "id", required = false) final Long id, @RequestBody Sashimi sashimi)
        throws URISyntaxException {
        log.debug("REST request to update Sashimi : {}, {}", id, sashimi);
        if (sashimi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sashimi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sashimiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sashimi result = sashimiRepository.save(sashimi);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sashimi.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sashimis/:id} : Partial updates given fields of an existing sashimi, field will ignore if it is null
     *
     * @param id the id of the sashimi to save.
     * @param sashimi the sashimi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sashimi,
     * or with status {@code 400 (Bad Request)} if the sashimi is not valid,
     * or with status {@code 404 (Not Found)} if the sashimi is not found,
     * or with status {@code 500 (Internal Server Error)} if the sashimi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sashimis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sashimi> partialUpdateSashimi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Sashimi sashimi
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sashimi partially : {}, {}", id, sashimi);
        if (sashimi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sashimi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sashimiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sashimi> result = sashimiRepository
            .findById(sashimi.getId())
            .map(existingSashimi -> {
                if (sashimi.getNome() != null) {
                    existingSashimi.setNome(sashimi.getNome());
                }
                if (sashimi.getDescricao() != null) {
                    existingSashimi.setDescricao(sashimi.getDescricao());
                }
                if (sashimi.getImagem() != null) {
                    existingSashimi.setImagem(sashimi.getImagem());
                }
                if (sashimi.getImagemContentType() != null) {
                    existingSashimi.setImagemContentType(sashimi.getImagemContentType());
                }
                if (sashimi.getPreco() != null) {
                    existingSashimi.setPreco(sashimi.getPreco());
                }
                if (sashimi.getPromocao() != null) {
                    existingSashimi.setPromocao(sashimi.getPromocao());
                }
                if (sashimi.getAtivo() != null) {
                    existingSashimi.setAtivo(sashimi.getAtivo());
                }

                return existingSashimi;
            })
            .map(sashimiRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sashimi.getId().toString())
        );
    }

    /**
     * {@code GET  /sashimis} : get all the sashimis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sashimis in body.
     */
    @GetMapping("/sashimis")
    public List<Sashimi> getAllSashimis() {
        log.debug("REST request to get all Sashimis");
        return sashimiRepository.findAll();
    }

    /**
     * {@code GET  /sashimis/:id} : get the "id" sashimi.
     *
     * @param id the id of the sashimi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sashimi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sashimis/{id}")
    public ResponseEntity<Sashimi> getSashimi(@PathVariable Long id) {
        log.debug("REST request to get Sashimi : {}", id);
        Optional<Sashimi> sashimi = sashimiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sashimi);
    }

    /**
     * {@code DELETE  /sashimis/:id} : delete the "id" sashimi.
     *
     * @param id the id of the sashimi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sashimis/{id}")
    public ResponseEntity<Void> deleteSashimi(@PathVariable Long id) {
        log.debug("REST request to delete Sashimi : {}", id);
        sashimiRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
