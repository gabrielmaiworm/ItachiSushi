package com.maiworm.sushi.web.rest;

import com.maiworm.sushi.domain.Entrada;
import com.maiworm.sushi.repository.EntradaRepository;
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
 * REST controller for managing {@link com.maiworm.sushi.domain.Entrada}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EntradaResource {

    private final Logger log = LoggerFactory.getLogger(EntradaResource.class);

    private static final String ENTITY_NAME = "entrada";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntradaRepository entradaRepository;

    public EntradaResource(EntradaRepository entradaRepository) {
        this.entradaRepository = entradaRepository;
    }

    /**
     * {@code POST  /entradas} : Create a new entrada.
     *
     * @param entrada the entrada to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entrada, or with status {@code 400 (Bad Request)} if the entrada has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entradas")
    public ResponseEntity<Entrada> createEntrada(@RequestBody Entrada entrada) throws URISyntaxException {
        log.debug("REST request to save Entrada : {}", entrada);
        if (entrada.getId() != null) {
            throw new BadRequestAlertException("A new entrada cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Entrada result = entradaRepository.save(entrada);
        return ResponseEntity
            .created(new URI("/api/entradas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entradas/:id} : Updates an existing entrada.
     *
     * @param id the id of the entrada to save.
     * @param entrada the entrada to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entrada,
     * or with status {@code 400 (Bad Request)} if the entrada is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entrada couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entradas/{id}")
    public ResponseEntity<Entrada> updateEntrada(@PathVariable(value = "id", required = false) final Long id, @RequestBody Entrada entrada)
        throws URISyntaxException {
        log.debug("REST request to update Entrada : {}, {}", id, entrada);
        if (entrada.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entrada.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entradaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Entrada result = entradaRepository.save(entrada);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, entrada.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /entradas/:id} : Partial updates given fields of an existing entrada, field will ignore if it is null
     *
     * @param id the id of the entrada to save.
     * @param entrada the entrada to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entrada,
     * or with status {@code 400 (Bad Request)} if the entrada is not valid,
     * or with status {@code 404 (Not Found)} if the entrada is not found,
     * or with status {@code 500 (Internal Server Error)} if the entrada couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/entradas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Entrada> partialUpdateEntrada(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Entrada entrada
    ) throws URISyntaxException {
        log.debug("REST request to partial update Entrada partially : {}, {}", id, entrada);
        if (entrada.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entrada.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entradaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Entrada> result = entradaRepository
            .findById(entrada.getId())
            .map(existingEntrada -> {
                if (entrada.getNome() != null) {
                    existingEntrada.setNome(entrada.getNome());
                }
                if (entrada.getDescricao() != null) {
                    existingEntrada.setDescricao(entrada.getDescricao());
                }
                if (entrada.getImagem() != null) {
                    existingEntrada.setImagem(entrada.getImagem());
                }
                if (entrada.getImagemContentType() != null) {
                    existingEntrada.setImagemContentType(entrada.getImagemContentType());
                }
                if (entrada.getPreco() != null) {
                    existingEntrada.setPreco(entrada.getPreco());
                }
                if (entrada.getPromocao() != null) {
                    existingEntrada.setPromocao(entrada.getPromocao());
                }
                if (entrada.getAtivo() != null) {
                    existingEntrada.setAtivo(entrada.getAtivo());
                }

                return existingEntrada;
            })
            .map(entradaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, entrada.getId().toString())
        );
    }

    /**
     * {@code GET  /entradas} : get all the entradas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entradas in body.
     */
    @GetMapping("/entradas")
    public List<Entrada> getAllEntradas() {
        log.debug("REST request to get all Entradas");
        return entradaRepository.findAll();
    }

    /**
     * {@code GET  /entradas/:id} : get the "id" entrada.
     *
     * @param id the id of the entrada to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entrada, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entradas/{id}")
    public ResponseEntity<Entrada> getEntrada(@PathVariable Long id) {
        log.debug("REST request to get Entrada : {}", id);
        Optional<Entrada> entrada = entradaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(entrada);
    }

    /**
     * {@code DELETE  /entradas/:id} : delete the "id" entrada.
     *
     * @param id the id of the entrada to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entradas/{id}")
    public ResponseEntity<Void> deleteEntrada(@PathVariable Long id) {
        log.debug("REST request to delete Entrada : {}", id);
        entradaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
