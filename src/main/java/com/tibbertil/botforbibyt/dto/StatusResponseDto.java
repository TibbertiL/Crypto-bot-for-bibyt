package com.tibbertil.botforbibyt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusResponseDto {

    private String status;
    private String message;

    public static StatusResponseDto ok() {
        return StatusResponseDto.builder()
                .status("OK")
                .build();
    }
}
