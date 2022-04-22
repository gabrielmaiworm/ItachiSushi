package com.maiworm.sushi.web.rest;

import com.maiworm.sushi.domain.Sushi;
import com.maiworm.sushi.repository.SushiRepository;
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
 * REST controller for managing {@link com.maiworm.sushi.domain.Sushi}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SushiResource {

    private final Logger log = LoggerFactory.getLogger(SushiResource.class);

    private static final String ENTITY_NAME = "sushi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SushiRepository sushiRepository;

    public SushiResource(SushiRepository sushiRepository) {
        this.sushiRepository = sushiRepository;
    }

    /**
     * {@code POST  /sushis} : Create a new sushi.
     *
     * @param sushi the sushi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sushi, or with status {@code 400 (Bad Request)} if the sushi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sushis")
    public ResponseEntity<Sushi> createSushi(@RequestBody Sushi sushi) throws URISyntaxException {
        log.debug("REST request to save Sushi : {}", sushi);
        if (sushi.getId() != null) {
            throw new BadRequestAlertException("A new sushi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sushi result = sushiRepository.save(sushi);
        return ResponseEntity
            .created(new URI("/api/sushis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sushis/:id} : Updates an existing sushi.
     *
     * @param id the id of the sushi to save.
     * @param sushi the sushi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sushi,
     * or with status {@code 400 (Bad Request)} if the sushi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sushi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sushis/{id}")
    public ResponseEntity<Sushi> updateSushi(@PathVariable(value = "id", required = false) final Long id, @RequestBody Sushi sushi)
        throws URISyntaxException {
        log.debug("REST request to update Sushi : {}, {}", id, sushi);
        if (sushi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sushi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sushiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sushi result = sushiRepository.save(sushi);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sushi.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sushis/:id} : Partial updates given fields of an existing sushi, field will ignore if it is null
     *
     * @param id the id of the sushi to save.
     * @param sushi the sushi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sushi,
     * or with status {@code 400 (Bad Request)} if the sushi is not valid,
     * or with status {@code 404 (Not Found)} if the sushi is not found,
     * or with status {@code 500 (Internal Server Error)} if the sushi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sushis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sushi> partialUpdateSushi(@PathVariable(value = "id", required = false) final Long id, @RequestBody Sushi sushi)
        throws URISyntaxException {
        log.debug("REST request to partial update Sushi partially : {}, {}", id, sushi);
        if (sushi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sushi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sushiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sushi> result = sushiRepository
            .findById(sushi.getId())
            .map(existingSushi -> {
                if (sushi.getNome() != null) {
                    existingSushi.setNome(sushi.getNome());
                }
                if (sushi.getDescricao() != null) {
                    existingSushi.setDescricao(sushi.getDescricao());
                }
                if (sushi.getImagem() != null) {
                    existingSushi.setImagem(sushi.getImagem());
                }
                if (sushi.getImagemContentType() != null) {
                    existingSushi.setImagemContentType(sushi.getImagemContentType());
                }
                if (sushi.getPreco() != null) {
                    existingSushi.setPreco(sushi.getPreco());
                }
                if (sushi.getPromocao() != null) {
                    existingSushi.setPromocao(sushi.getPromocao());
                }
                if (sushi.getAtivo() != null) {
                    existingSushi.setAtivo(sushi.getAtivo());
                }

                return existingSushi;
            })
            .map(sushiRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sushi.getId().toString())
        );
    }

    /**
     * {@code GET  /sushis} : get all the sushis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sushis in body.
     */
    @GetMapping("/sushis")
    public List<Sushi> getAllSushis() {
        log.debug("REST request to get all Sushis");
        return sushiRepository.findAll();
    }

    /**
     * {@code GET  /sushis/:id} : get the "id" sushi.
     *
     * @param id the id of the sushi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sushi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sushis/{id}")
    public ResponseEntity<Sushi> getSushi(@PathVariable Long id) {
        log.debug("REST request to get Sushi : {}", id);
        Optional<Sushi> sushi = sushiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sushi);
    }

    /**
     * {@code DELETE  /sushis/:id} : delete the "id" sushi.
     *
     * @param id the id of the sushi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sushis/{id}")
    public ResponseEntity<Void> deleteSushi(@PathVariable Long id) {
        log.debug("REST request to delete Sushi : {}", id);
        sushiRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
