package com.bdpit5.bus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "error_code", "error_message" })
public class ErrorSchema {

    @JsonProperty("error_message")
    private ErrorMessage errorMessage;

    @JsonProperty("error_code")
    private String errorCode;

}

