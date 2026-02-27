package com.bdpit5.bus.service;

import com.bdpit5.bus.dto.*;
import com.bdpit5.bus.entity.UrlMappingEntity;
import com.bdpit5.bus.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlMappingRepository repository;

    @Value("${app.base-url}")
    private String BASE_URL;
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_CODE_LENGTH = 6;

    public UrlResponse createShortUrl(CreateUrlRequest request) {

        try {

            if (request.getOriginalUrl() == null || request.getOriginalUrl().isBlank()) {
                throw new IllegalArgumentException("Original URL must not be empty");
            }

            String shortCode;

            if (request.getCustomUrl() != null && !request.getCustomUrl().isBlank()) {
                shortCode = generateCustomURL(request.getCustomUrl());
            } else {
                shortCode = generateUniqueShortCode();
            }

            UrlMappingEntity mapping = UrlMappingEntity.builder()
                    .originalUrl(request.getOriginalUrl())
                    .shortCode(shortCode)
                    .build();

            repository.save(mapping);

            UrlResponse.OutputSchema output =
                    UrlResponse.OutputSchema.builder()
                            .originalUrl(mapping.getOriginalUrl())
                            .shortUrl(BASE_URL + shortCode)
                            .build();

            return buildResponse(
                    "200",
                    "Berhasil",
                    "Success",
                    List.of(output)
            );

        } catch (Exception e) {

            return buildResponse(
                    "500",
                    "Terjadi kesalahan pada sistem",
                    "Internal server error",
                    List.of()
            );
        }
    }

    private UrlResponse buildResponse(
            String code,
            String idMessage,
            String enMessage,
            List<UrlResponse.OutputSchema> outputs
    ) {

        ErrorMessage errorMessage =
                new ErrorMessage(idMessage, enMessage);

        ErrorSchema errorSchema =
                new ErrorSchema(errorMessage, code);

        return UrlResponse.builder()
                .errorSchema(errorSchema)
                .outputSchemas(outputs)
                .build();
    }

    public UrlMappingEntity getByShortCode(String code) {
        return repository.findByShortCode(code).orElse(null);
    }

    public UrlResponse getAllUrls() {
        try {
            List<UrlMappingEntity> entities = repository.findAll();

            List<UrlResponse.OutputSchema> outputs = entities.stream()
                    .map(entity -> UrlResponse.OutputSchema.builder()
                            .originalUrl(entity.getOriginalUrl())
                            .shortUrl(BASE_URL + entity.getShortCode())
                            .build())
                    .toList();

            return buildResponse(
                    "00",
                    "Berhasil",
                    "Success",
                    outputs
            );

        } catch (Exception e) {

            throw new IllegalArgumentException("Internal server error");
        }
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = generateRandomString();
        } while (repository.existsByShortCode(shortCode));
        return shortCode;
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(
                    random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    private String generateCustomURL(String customURL) {
        String shortCode = customURL;
        int counter = 1;

        while (repository.existsByShortCode(shortCode)) {
            shortCode = customURL + "_" + counter;
            counter++;
        }

        return shortCode;
    }
}