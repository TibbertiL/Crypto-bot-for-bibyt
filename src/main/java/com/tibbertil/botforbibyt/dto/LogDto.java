package com.tibbertil.botforbibyt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogDto {
    private String level;
    private String message;
    private LocalDateTime timestamp;
    private String context;
}
