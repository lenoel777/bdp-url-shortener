package com.bdpit5.bus.repository;

import com.bdpit5.bus.entity.QrCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrRepository extends JpaRepository<QrCodeEntity, Long> {
}