package com.spm.controller;

import com.spm.dto.UserLoginDTO;
import com.spm.dto.UserRegistrationDTO;
import com.spm.entity.UserDetail;
import com.spm.service.UserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserDetailService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Success() {
        UserDetail userDetail = new UserDetail();
        userDetail.setUserName("testUser");
        userDetail.setPassword("password");

        UserRegistrationDTO registrationResponse = new UserRegistrationDTO();
        registrationResponse.setUserName("testUser");

        when(userService.registerUser(any(UserDetail.class))).thenReturn(registrationResponse);

        UserRegistrationDTO response = userController.register(userDetail);

        assertEquals(registrationResponse.getUserName(), response.getUserName());
    }

    @Test
    public void testLogin_Success() {
        UserLoginDTO loginRequest = new UserLoginDTO();
        loginRequest.setUserName("testUser");
        loginRequest.setPassword("password");

        UserRegistrationDTO loginResponse = new UserRegistrationDTO();
        loginResponse.setUserName("testUser");


        when(userService.loginUser(any(UserLoginDTO.class))).thenReturn(loginResponse);

        UserRegistrationDTO response = userController.getUserById(loginRequest);

        assertEquals(loginResponse.getUserName(), response.getUserName());

    }

}

