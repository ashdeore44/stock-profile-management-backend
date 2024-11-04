package com.spm.repository;

import com.spm.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByUserName(String userName);

    Optional<UserDetail> findByEmail(String email);
    Optional<UserDetail> findByEmailAndPassword(String email,String password);
}

