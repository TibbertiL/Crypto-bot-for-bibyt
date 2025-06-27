package com.tibbertil.botforbibyt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderDto {

    private String symbol;
    private String orderType;
    private BigDecimal price;
    private BigDecimal quantity;
    private String status;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("execute_at")
    private LocalDateTime executedAt;
    @NaturalId
    @JsonProperty("order_id")
    private UUID orderId;
}
