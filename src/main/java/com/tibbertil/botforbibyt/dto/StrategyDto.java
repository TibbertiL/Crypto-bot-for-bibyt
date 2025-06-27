package com.tibbertil.botforbibyt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StrategyDto {

    private Long id;
    private String symbol;
    private Boolean active;
    @JsonProperty("buy_below")
    private BigDecimal buyBelow;
    @JsonProperty("sell_below")
    private BigDecimal sellAbove;
    @JsonProperty("order_amount")
    private BigDecimal orderAmount;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
