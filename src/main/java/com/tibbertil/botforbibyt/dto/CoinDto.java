package com.tibbertil.botforbibyt.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CoinDto {

    private String symbol;
    private BigDecimal price;
    private LocalDateTime timestamp;
}
