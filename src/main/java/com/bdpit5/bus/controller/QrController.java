package com.bdpit5.bus.controller;

import com.bdpit5.bus.dto.QrRequest;
import com.bdpit5.bus.dto.QrResponse;
import com.bdpit5.bus.service.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;

    @GetMapping("/inquiry")
    public ResponseEntity<QrResponse> inquiryAll() {
        return ResponseEntity.ok(qrService.inquiryAll());
    }

    @PostMapping("/generate")
    public ResponseEntity<QrResponse> generateQr(
            @RequestBody QrRequest request
    ) {
        return ResponseEntity.ok(qrService.generate(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QrResponse> deleteQr(@PathVariable Long id) {
        return ResponseEntity.ok(qrService.deleteById(id));
    }

    @DeleteMapping("/all")
    public ResponseEntity<QrResponse> deleteAllQr() {
        return ResponseEntity.ok(qrService.deleteAllQr());
    }
}