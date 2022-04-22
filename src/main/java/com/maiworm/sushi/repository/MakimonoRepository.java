package com.maiworm.sushi.repository;

import com.maiworm.sushi.domain.Makimono;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Makimono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MakimonoRepository extends JpaRepository<Makimono, Long> {}
