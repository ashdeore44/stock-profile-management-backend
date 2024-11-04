package com.spm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinalPortfolioDTO {

    private Long portfolioId;
    private String userName;
    private List<StockDTO> stocks;


    public FinalPortfolioDTO(String userName, List<StockDTO> stocks) {
        this.userName = userName;
        this.stocks = stocks;
    }
}
