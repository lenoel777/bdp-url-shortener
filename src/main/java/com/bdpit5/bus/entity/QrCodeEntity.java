package com.bdpit5.bus.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
    @Table(name = "qr_code")
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class QrCodeEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String originalUrl;

        @Column(length = 10000)
        private String qrCodeBase64;

        private LocalDateTime createdAt;
    }
