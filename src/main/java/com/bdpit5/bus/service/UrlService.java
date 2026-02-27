package com.bdpit5.bus.service;

import com.bdpit5.bus.dto.CreateUrlRequest;
import com.bdpit5.bus.dto.UrlResponse;
import com.bdpit5.bus.entity.UrlMapping;
import com.bdpit5.bus.repository.UrlMappingRepository;
import jakarta.annotation.PostConstruct;
import lombok.*;
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
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_CODE_LENGTH = 6;

    public UrlResponse createShortUrl(CreateUrlRequest request) {
        String shortCode;

        if (request.getCustomUrl() != null && !request.getCustomUrl().isBlank()) {
            shortCode = generateUniqueCustomShortCode(request.getCustomUrl());
        } else {
            shortCode = generateUniqueShortCode();
        }

        UrlMapping mapping = UrlMapping.builder()
                .originalUrl(request.getOriginalUrl())
                .shortCode(shortCode)
                .build();

        repository.save(mapping);

        return UrlResponse.builder()
                .originalUrl(request.getOriginalUrl())
                .shortUrl(BASE_URL + shortCode)
                .build();
    }

    public String getOriginalUrl(String shortCode) {
        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"))
                .getOriginalUrl();
    }

    public List<UrlMapping> getAllUrls() {
        return repository.findAll();
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
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    private String generateUniqueCustomShortCode(String customUrl) {
        String shortCode;
        int counter = 1;
        if (!repository.existsByShortCode(customUrl)) {
            return customUrl;
        }

        do {
            shortCode = customUrl + "_" + counter;
            counter++;
        } while (repository.existsByShortCode(shortCode));

        return shortCode;
    }

    @PostConstruct
    public void logProfile() {
        System.out.println("BASE URL: " + BASE_URL);
    }
}