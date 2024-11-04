package com.spm.repository;

import com.spm.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByTicker(String ticker);

    @Modifying
    @Query("UPDATE Stock s SET s.ticker = :ticker, s.name = :name, s.price = :price WHERE s.id = :id")
    int updateStock(@Param("id") Long stockId,
                    @Param("ticker") String ticker,
                    @Param("name") String name,
                    @Param("price") Double price);
}


