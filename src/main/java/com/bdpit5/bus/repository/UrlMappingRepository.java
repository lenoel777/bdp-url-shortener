package com.bdpit5.bus.repository;
import com.bdpit5.bus.entity.UrlMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMappingEntity, Long> {

    Optional<UrlMappingEntity> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);
}