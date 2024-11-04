package com.spm.repository;

import com.spm.dto.PortfolioDTO;
import com.spm.entity.Portfolio;
import com.spm.entity.UserDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUser(UserDetail user);

    @Query("SELECT new com.spm.dto.PortfolioDTO(" +
            "p.id, u.id, u.userName, s.id,s.ticker, p.quantity, s.price, (p.quantity * s.price)) " +
            "FROM Portfolio p " +
            "JOIN p.user u " +
            "JOIN p.stock s " +
            "WHERE u.id = :userId")
    List<PortfolioDTO> findPortfolioByUserId(@Param("userId") Long userId);

    @Query("SELECT p.quantity FROM Portfolio p WHERE p.user.id = :userId AND p.stock.id = :stockId")
    Integer findStockQuantity(@Param("userId") Long userId,@Param("stockId") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Portfolio p SET p.quantity = :quantity WHERE p.user.id = :userId AND p.stock.id = :stockId")
    int updateQuantity(@Param("userId") Long userId,@Param("stockId") Long id,@Param("quantity") Integer quantity);


}


