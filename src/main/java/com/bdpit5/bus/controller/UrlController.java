package com.bdpit5.bus.controller;

import com.bdpit5.bus.dto.CreateUrlRequest;
import com.bdpit5.bus.dto.UrlResponse;
import com.bdpit5.bus.entity.UrlMappingEntity;
import com.bdpit5.bus.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService service;

    @PostMapping("/api/urls")
    public ResponseEntity<UrlResponse> createShortUrl(@Valid @RequestBody CreateUrlRequest request) {
        return ResponseEntity.ok(service.createShortUrl(request));
    }

    @GetMapping("/s/{code}")
    public ResponseEntity<?> redirect(@PathVariable String code) {

        UrlMappingEntity url = service.getByShortCode(code);

        if (url == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url.getOriginalUrl()))
                .build();
    }

    @GetMapping("/api/urls")
    public ResponseEntity<UrlResponse> getAllUrls() {
        return ResponseEntity.ok(service.getAllUrls());
    }

}