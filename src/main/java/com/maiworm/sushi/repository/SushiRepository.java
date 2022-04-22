package com.maiworm.sushi.repository;

import com.maiworm.sushi.domain.Sushi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Sushi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SushiRepository extends JpaRepository<Sushi, Long> {}
