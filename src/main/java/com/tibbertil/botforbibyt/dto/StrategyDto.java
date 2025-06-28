package com.tibbertil.botforbibyt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StrategyDto {

    private Long id;
    @NotBlank(message = "Необходимо указать символ монеты для стратегии")
    private String symbol;
    private Boolean active = false;
    @NotNull(message = "Для стратегии укажите при какой цене вы бы хотели приобрести монету")
    @Min(value = 0)
    @JsonProperty("buy_below")
    private BigDecimal buyBelow;
    @NotNull(message = "Для стратегии укажите при какой цене вы бы хотели продать монету")
    @Min(value = 0)
    @JsonProperty("sell_below")
    private BigDecimal sellAbove;
    @NotNull(message = "Для стратегии укажите количество монет, которое собираетесь приобрести")
    @Min(value = 1)
    @JsonProperty("order_amount")
    private BigDecimal orderAmount;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @AssertTrue(message = "Цена продажи должна быть выше цены покупки")
    public boolean isSellAboveBuyBelow() {
        return sellAbove.compareTo(buyBelow) > 0;
    }
}
