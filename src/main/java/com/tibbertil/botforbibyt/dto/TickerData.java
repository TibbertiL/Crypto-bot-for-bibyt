package com.tibbertil.botforbibyt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TickerData {
    private String symbol;
    @JsonProperty("last_price")
    private String lastPrice;
}
