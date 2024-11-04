package com.spm.service;

import com.spm.config.SecurityConfig;
import com.spm.dto.UserLoginDTO;
import com.spm.dto.UserRegistrationDTO;
import com.spm.entity.UserDetail;
import com.spm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    public UserRegistrationDTO registerUser(UserDetail userDetail) {
        if (userRepository.findByUserName(userDetail.getUserName()).isPresent()) {
            throw new IllegalArgumentException("User is already registered.");
        }
        try {
            userDetail.setPassword(securityConfig.encrypt(userDetail.getPassword())); // Hash the password
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("calling user register");
        return mapUserDetailDto(userRepository.save(userDetail));
    }

    private UserRegistrationDTO mapUserDetailDto(UserDetail userDetail){
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setEmail(userDetail.getEmail());
        dto.setUserName(userDetail.getUserName());
        return dto;

    }

    public UserRegistrationDTO loginUser(UserLoginDTO userLoginDTO)  {
        Optional<UserDetail> userOptional = userRepository.findByUserName(userLoginDTO.getUserName());
        System.out.println("calling loginUser");
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid user");
        }

        UserDetail userDetail = userOptional.get();


        try {
            if (!SecurityConfig.encrypt(userLoginDTO.getPassword()).equals(userDetail.getPassword())) {
                throw new IllegalArgumentException("Invalid password");
            }
            System.out.println("Password match");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mapUserDetailDto(userDetail);
    }

}
