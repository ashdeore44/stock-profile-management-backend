package com.spm.service;

import com.spm.dto.FinalPortfolioDTO;
import com.spm.dto.PortfolioDTO;
import com.spm.dto.StockDTO;
import com.spm.entity.UserDetail;
import com.spm.repository.*;
import com.spm.entity.Portfolio;
import com.spm.entity.Stock;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserRepository userRepository;

    private final String API_KEY = "Z5XNY2MAJKGCOLBY"; // Replace with your actual API key
    private final String BASE_URL = "https://www.alphavantage.co/query";

    @Transactional
    public Portfolio addStockToPortfolio(StockDTO stockDTO) {

    UserDetail userDetail = userRepository.findByUserName(stockDTO.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    Stock stock = fetchRealTimeStockPrice(stockDTO.getTicker());
        Portfolio portfolio = new Portfolio();
    if(stock.getTicker()==null){
        throw new RuntimeException("stock not found");
    }else{

        Optional<Stock> stockRecord =stockRepository.findByTicker(stockDTO.getTicker());
        if(stockRecord.isPresent()){

                System.out.println("stockRecord --> " + stockRecord.toString());
                stockRepository.updateStock(stockRecord.get().getId(),
                        stockRecord.get().getTicker(),stockRecord.get().getName(),stockRecord.get().getPrice());
                portfolio.setStock(stockRecord.get());
                portfolio.setUser(userDetail);

                Integer exstingQuantity = portfolioRepository.findStockQuantity(userDetail.getId(), stockRecord.get().getId());
                if(exstingQuantity!=null) {
                    System.out.println("exstingQuantity ----> " + exstingQuantity);

                    portfolioRepository.updateQuantity(userDetail.getId(), stockRecord.get().getId(), exstingQuantity + stockDTO.getQuantity());
                }
                return portfolio;


        }else{
            System.out.println("Stock not found");
            portfolio.setStock(stock);
            stockRepository.save(stock);
            portfolio.setUser(userDetail);
            portfolio.setQuantity(stockDTO.getQuantity());
            portfolioRepository.save(portfolio);
            return portfolio;
        }
    }
    }
    public void removeStockFromPortfolio(StockDTO stockDTO) {

        System.out.println("stockDTO ----> "+stockDTO.toString());
        Integer inputQuantity = stockDTO.getQuantity();

        UserDetail userDetail = userRepository.findByUserName(stockDTO.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Stock stock = stockRepository.findByTicker(stockDTO.getTicker())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        Portfolio portfolio = portfolioRepository.findByUser(userDetail).stream()
                .filter(p -> p.getStock().equals(stock))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Stock not in portfolio"));

        if(portfolio.getQuantity()>=inputQuantity){
            Integer currentQuantity = portfolio.getQuantity()-inputQuantity;
            portfolio.setQuantity(currentQuantity);
            portfolioRepository.save(portfolio);
        }else{
            System.out.println("You can not remove stock more than exsiting quantiy --->"+portfolio.getQuantity());

        }

    }

    public List<Portfolio> getPortfolio(String username) {
        UserDetail user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Portfolio> portfolioList = portfolioRepository.findByUser(user);
        return portfolioList;
    }

    public FinalPortfolioDTO getPortfolioCustom(String username) {
        UserDetail user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<PortfolioDTO> portfolioList = portfolioRepository.findPortfolioByUserId(user.getId());

        FinalPortfolioDTO finalPortfolioDTO =convertToUserPortfolio(portfolioList.stream()
                .map(portfolio -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("portfolioId", portfolio.getPortfolioId());
                    map.put("userId", portfolio.getUserId());
                    map.put("userName", portfolio.getUserName());
                    map.put("stockId", portfolio.getStockId());
                    map.put("ticker", portfolio.getTicker());
                    map.put("quantity", portfolio.getQuantity());
                    map.put("price", portfolio.getPrice());
                    map.put("totalValue", portfolio.getQuantity()*portfolio.getPrice());
                    return map;
                })
                .collect(Collectors.toList()));
        return finalPortfolioDTO;
    }

    public double calculateTotalPortfolioValue(String username) {
        List<Portfolio> portfolioList = getPortfolio(username);
        double totalValue = 0.0;
        for (Portfolio entry : portfolioList) {
            Stock stockPrice = fetchRealTimeStockPrice(entry.getStock().getTicker());

            totalValue += stockPrice.getPrice() * entry.getQuantity();
        }
        return totalValue;
    }

    public Stock fetchRealTimeStockPrice(String ticker) {
        Stock stock = new Stock();
        try {

            String url = BASE_URL + "?function=GLOBAL_QUOTE&symbol=" + ticker + "&apikey=" + API_KEY;

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            JSONObject jsonResponse = new JSONObject(response);
            JSONObject globalQuote = jsonResponse.getJSONObject("Global Quote");

            String priceString = globalQuote.getString("05. price");
            stock.setPrice(Double.parseDouble(priceString));
            stock.setTicker(globalQuote.getString("01. symbol"));

            //mocking
/*            stock.setPrice(100.00);
            stock.setName("BA");
            stock.setTicker("BA");*/


            return stock;

            } catch (Exception e) {
                e.printStackTrace();
                return stock;
            }
        }




    public static FinalPortfolioDTO convertToUserPortfolio(List<Map<String, Object>> data) {
        if (data.isEmpty()) {
            return null;
        }

        Long userId = (Long) data.get(0).get("userId");
        String userName = (String) data.get(0).get("userName");

        List<StockDTO> stocks = data.stream()
                .map(stock -> new StockDTO(
                        (Long) stock.get("stockId"),
                        (String) stock.get("ticker"),
                        (Integer) stock.get("quantity"),
                        (Double) stock.get("price"),
                        (Double) stock.get("totalValue")))
                .collect(Collectors.toList());

        return new FinalPortfolioDTO( userName, stocks);
    }



}

