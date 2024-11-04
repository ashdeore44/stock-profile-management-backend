package com.spm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockDTO {

    private String userName;
    private String ticker;
    private Integer quantity;
    private Double price;

    private Long stockId;

    private Double totalValue;

    public StockDTO(Long stockId, String ticker, Integer quantity, Double price, Double totalValue) {
        this.stockId = stockId;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.totalValue = totalValue;
    }
}
