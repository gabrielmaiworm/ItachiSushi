package com.maiworm.sushi.repository;

import com.maiworm.sushi.domain.Yakisoba;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Yakisoba entity.
 */
@SuppressWarnings("unused")
@Repository
public interface YakisobaRepository extends JpaRepository<Yakisoba, Long> {}
