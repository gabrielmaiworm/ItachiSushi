package com.maiworm.sushi.repository;

import com.maiworm.sushi.domain.Temaki;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Temaki entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemakiRepository extends JpaRepository<Temaki, Long> {}
