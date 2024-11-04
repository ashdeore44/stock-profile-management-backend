package com.spm.controller;

import com.spm.dto.FinalPortfolioDTO;
import com.spm.dto.StockDTO;
import com.spm.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping("/add")
    public FinalPortfolioDTO addStock(@RequestBody StockDTO stockDTO, @RequestParam String userName) {
        portfolioService.addStockToPortfolio(stockDTO);
        FinalPortfolioDTO portfolio = portfolioService.getPortfolioCustom(userName);
        return portfolio;
    }

    @PostMapping("/remove")
    public FinalPortfolioDTO removeStock(@RequestBody StockDTO stockDTO) {
        portfolioService.removeStockFromPortfolio(stockDTO);
        FinalPortfolioDTO portfolio = portfolioService.getPortfolioCustom(stockDTO.getUserName());
        return portfolio;
    }

    @GetMapping
    public FinalPortfolioDTO getPortfolio(@RequestParam String userName) {
        FinalPortfolioDTO portfolio = portfolioService.getPortfolioCustom(userName);
        return portfolio;
    }

    @GetMapping("/value")
    public Double calculateTotalValue(@RequestParam String userName) {
        double totalValue = portfolioService.calculateTotalPortfolioValue(userName);
        System.out.println("totalValue -----> "+totalValue);
        return totalValue;
    }
}


