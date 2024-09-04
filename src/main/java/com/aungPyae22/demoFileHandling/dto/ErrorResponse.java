package com.aungPyae22.demoFileHandling.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private Long timestamp;
}
