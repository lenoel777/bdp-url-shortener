package com.bdpit5.bus.controller;

import com.bdpit5.bus.dto.CreateUrlRequest;
import com.bdpit5.bus.dto.UrlResponse;
import com.bdpit5.bus.entity.UrlMapping;
import com.bdpit5.bus.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService service;

    @PostMapping("/api/urls")
    public ResponseEntity<UrlResponse> createShortUrl(@Valid @RequestBody CreateUrlRequest request) {
        return ResponseEntity.ok(service.createShortUrl(request));
    }

    @GetMapping("/s/{shortCode}")
    public void redirect(@PathVariable String shortCode,
                         HttpServletResponse response) throws IOException {
        String originalUrl = service.getOriginalUrl(shortCode);
        response.sendRedirect(originalUrl);
    }

    @GetMapping("/api/urls")
    public ResponseEntity<List<UrlMapping>> getAllUrls() {
        return ResponseEntity.ok(service.getAllUrls());
    }
}