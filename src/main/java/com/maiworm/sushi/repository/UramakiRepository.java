package com.maiworm.sushi.repository;

import com.maiworm.sushi.domain.Uramaki;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Uramaki entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UramakiRepository extends JpaRepository<Uramaki, Long> {}
