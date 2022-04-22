package com.maiworm.sushi.repository;

import com.maiworm.sushi.domain.Especiais;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Especiais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EspeciaisRepository extends JpaRepository<Especiais, Long> {}
