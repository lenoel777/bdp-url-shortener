package com.bdpit5.bus.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "english", "indonesia" })
public class ErrorMessage {
    private String indonesian;
    private String english;
}


