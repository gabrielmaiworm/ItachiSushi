package com.maiworm.sushi.repository;

import com.maiworm.sushi.domain.Hot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Hot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HotRepository extends JpaRepository<Hot, Long> {}
