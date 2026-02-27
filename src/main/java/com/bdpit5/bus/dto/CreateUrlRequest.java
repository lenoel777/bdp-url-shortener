package com.bdpit5.bus.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CreateUrlRequest {
    @NotBlank
    private String originalUrl;
    private String customUrl;
}