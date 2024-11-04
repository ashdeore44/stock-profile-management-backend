package com.spm.controller;

import com.spm.dto.FinalPortfolioDTO;
import com.spm.dto.StockDTO;
import com.spm.entity.Stock;
import com.spm.service.PortfolioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PortfolioControllerTest {

    @Mock
    private PortfolioService portfolioService;

    @InjectMocks
    private PortfolioController portfolioController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddStock() {
        StockDTO stockDTO = new StockDTO(1L, "BA",10,100.0, 1000.0);
        String userName = "testUser";
        List<StockDTO> stockDTOList= new ArrayList<>();
        stockDTOList.add(stockDTO);
        FinalPortfolioDTO expectedPortfolio = new FinalPortfolioDTO("testUser",stockDTOList);

        when(portfolioService.getPortfolioCustom(userName)).thenReturn(expectedPortfolio);

        FinalPortfolioDTO result = portfolioController.addStock(stockDTO, userName);

        verify(portfolioService, times(1)).addStockToPortfolio(stockDTO);
        verify(portfolioService, times(1)).getPortfolioCustom(userName);
        assertEquals(expectedPortfolio, result);
    }

    @Test
    public void testRemoveStock() {
        StockDTO stockDTO = new StockDTO(1L, "BA",10,100.0, 1000.0);
        stockDTO.setUserName("testUser");
        List<StockDTO> stockDTOList= new ArrayList<>();
        stockDTOList.add(stockDTO);
        FinalPortfolioDTO expectedPortfolio = new FinalPortfolioDTO("testUser",stockDTOList);

        when(portfolioService.getPortfolioCustom(stockDTO.getUserName())).thenReturn(expectedPortfolio);

        FinalPortfolioDTO result = portfolioController.removeStock(stockDTO);

        verify(portfolioService, times(1)).removeStockFromPortfolio(stockDTO);
        verify(portfolioService, times(1)).getPortfolioCustom(stockDTO.getUserName());
        assertEquals(expectedPortfolio, result);
    }

    @Test
    public void testGetPortfolio() {
        StockDTO stockDTO = new StockDTO(1L, "BA",10,100.0, 1000.0);
        stockDTO.setUserName("testUser");
        List<StockDTO> stockDTOList= new ArrayList<>();
        stockDTOList.add(stockDTO);
        FinalPortfolioDTO expectedPortfolio = new FinalPortfolioDTO("testUser",stockDTOList);

        when(portfolioService.getPortfolioCustom(stockDTO.getUserName())).thenReturn(expectedPortfolio);
        FinalPortfolioDTO result= portfolioController.getPortfolio("testUser");

        assertEquals(expectedPortfolio, result);
    }

    @Test
    public void testCalculateTotalValue() {
        Double totalValue = 1000.0;
        Stock stock = new Stock();
        stock.setPrice(100.00);
        stock.setTicker("BA");

        when(portfolioService.calculateTotalPortfolioValue("testUser")).thenReturn(totalValue);
        Double result = portfolioController.calculateTotalValue("testUser");
        assertEquals(totalValue, result);
    }
}

