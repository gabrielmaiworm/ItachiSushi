package com.maiworm.sushi.repository;

import com.maiworm.sushi.domain.Harumaki;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Harumaki entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HarumakiRepository extends JpaRepository<Harumaki, Long> {}
