package com.tibbertil.botforbibyt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BybitPriceResponseDto {

    @JsonProperty("ret_code")
    private String retCode;
    @JsonProperty("ret_msg")
    private String retMsg;
    private List<TickerData> result;
}
