package com.spm.controller;

import com.spm.dto.UserLoginDTO;
import com.spm.dto.UserRegistrationDTO;
import com.spm.entity.UserDetail;
import com.spm.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class UserController {

    @Autowired
    private UserDetailService userService;

    @PostMapping("/register")
    public UserRegistrationDTO register(@RequestBody UserDetail userDetails) {
        System.out.println("userDetails:::"+userDetails);
       return userService.registerUser(userDetails);
    }


    @PostMapping("/login")
    public UserRegistrationDTO getUserById(@RequestBody UserLoginDTO userLoginDTO) {
        return userService.loginUser(userLoginDTO);
    }

}
