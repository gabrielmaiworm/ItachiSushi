package com.maiworm.sushi.repository;

import com.maiworm.sushi.domain.Sashimi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sashimi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SashimiRepository extends JpaRepository<Sashimi, Long> {}
