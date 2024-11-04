package com.spm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PortfolioDTO {

    private Long portfolioId;
    private Long userId;
    private String userName;
    private Long stockId;

    private String ticker;
    private Integer quantity;
    private Double price;
    private Double totalValue;

    public PortfolioDTO(Long portfolioId, Long userId, String userName, Long stockId,String ticker, Integer quantity, Double price, Double totalValue) {
        this.portfolioId = portfolioId;
        this.userId = userId;
        this.userName = userName;
        this.stockId = stockId;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.totalValue = totalValue;
    }
}
