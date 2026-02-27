package com.bdpit5.bus.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QrRequest {
    @NotBlank(message = "Url must not be blank")
    private String originalUrl;
    private Integer size;
}
