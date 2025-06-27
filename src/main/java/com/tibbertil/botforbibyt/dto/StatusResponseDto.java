package com.tibbertil.botforbibyt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusResponseDto {

    private String status;
    private String message;

    public static StatusResponseDto ok() {
        StatusResponseDto statusResponseDto = new StatusResponseDto();
        statusResponseDto.setStatus("OK");
        return statusResponseDto;
    }
}
