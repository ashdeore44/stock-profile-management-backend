package com.spm.service;

import com.spm.dto.FinalPortfolioDTO;
import com.spm.dto.PortfolioDTO;
import com.spm.dto.StockDTO;
import com.spm.entity.Portfolio;
import com.spm.entity.Stock;
import com.spm.entity.UserDetail;
import com.spm.repository.PortfolioRepository;
import com.spm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PortfolioService portfolioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPortfolio() {

        String username = "testUser";
        UserDetail user = new UserDetail();
        user.setUserName(username);
        user.setPassword("password");

        Stock stock =new Stock();
        stock.setTicker("BA");
        stock.setPrice(100.0);
        stock.setId(1L);

        Portfolio portfolio=new Portfolio();
        portfolio.setQuantity(1);
        portfolio.setStock(stock);
        portfolio.setUser(user);

        List<Portfolio> mockPortfolios = new ArrayList<>();
        mockPortfolios.add(portfolio);

        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
        when(portfolioRepository.findByUser(user)).thenReturn(mockPortfolios);

        List<Portfolio> result = portfolioService.getPortfolio(username);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUserName(username);
        verify(portfolioRepository, times(1)).findByUser(user);
    }

    @Test
    void testGetPortfolioCustom() {

        String username = "testUser";
        UserDetail user = new UserDetail();
        user.setUserName(username);
        user.setPassword("password");
        user.setId(1L);

        Stock stock =new Stock();
        stock.setTicker("BA");
        stock.setPrice(100.0);
        stock.setId(1L);

        Portfolio portfolio=new Portfolio();
        portfolio.setQuantity(1);
        portfolio.setStock(stock);
        portfolio.setUser(user);

        PortfolioDTO portfolioDTO=new PortfolioDTO();
        portfolioDTO.setPortfolioId(1L);
        portfolioDTO.setQuantity(10);
        portfolioDTO.setTotalValue(1500.00);
        portfolioDTO.setPrice(150.00);
        portfolioDTO.setUserId(1L);

        List<PortfolioDTO> mockPortfolios = new ArrayList<>();
        mockPortfolios.add(portfolioDTO);

        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
        when(portfolioRepository.findPortfolioByUserId(user.getId())).thenReturn(mockPortfolios);

        FinalPortfolioDTO result = portfolioService.getPortfolioCustom(username);

        assertNotNull(result);
        verify(userRepository, times(1)).findByUserName(username);
        verify(portfolioRepository, times(1)).findPortfolioByUserId(user.getId());
    }

    @Test
    void testConvertToUserPortfolio_EmptyData() {

        List<Map<String, Object>> data = new ArrayList<>();
        FinalPortfolioDTO result = portfolioService.convertToUserPortfolio(data);
        assertNull(result);
    }

    @Test
    void testConvertToUserPortfolio_ValidData() {

        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> stockData1 = new HashMap<>();
        stockData1.put("userId", 1L);
        stockData1.put("userName", "testUser");
        stockData1.put("stockId", 1L);
        stockData1.put("ticker", "BA");
        stockData1.put("quantity", 10);
        stockData1.put("price", 150.0);
        stockData1.put("totalValue", 1500.0);

        data.add(stockData1);

        FinalPortfolioDTO result = PortfolioService.convertToUserPortfolio(data);

        assertNotNull(result);
        assertEquals("testUser", result.getUserName());
        assertEquals(1, result.getStocks().size());

        StockDTO stock1 = result.getStocks().get(0);
        assertEquals(1L, stock1.getStockId());
        assertEquals("BA", stock1.getTicker());
        assertEquals(10, stock1.getQuantity());
        assertEquals(150.0, stock1.getPrice());
        assertEquals(1500.0, stock1.getTotalValue());

    }

    @Test
    void testFetchStockData() {

        String symbol = "BA";
        Stock stock = new Stock();
        stock.setTicker("BA");
        stock.setPrice(100.00);

        String mockApiResponse = "{ 'Global Quote': { '01. symbol': 'BA', '05. price': '150.00' } }";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockApiResponse);

        Stock result = portfolioService.fetchRealTimeStockPrice("BA");

        assertNotNull(result);

    }

}

