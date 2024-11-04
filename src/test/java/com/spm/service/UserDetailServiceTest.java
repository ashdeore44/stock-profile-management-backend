package com.spm.service;

import com.spm.dto.UserLoginDTO;
import com.spm.dto.UserRegistrationDTO;
import com.spm.entity.UserDetail;
import com.spm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailService userDetailService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        UserDetail userDetails = new UserDetail();
        userDetails.setUserName("testUser");
        userDetails.setPassword("password");
        when(userRepository.save(any(UserDetail.class))).thenReturn(userDetails);

        UserRegistrationDTO result = userDetailService.registerUser(userDetails);

        verify(userRepository, times(1)).save(userDetails);
    }

   // @Test
    public void testloginUser() throws Exception {
        // Arrange
        String username = "testUser";
        UserDetail userDetails = new UserDetail();
        userDetails.setUserName(username);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(userDetails));

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserName(username);
        userLoginDTO.setPassword("password");
        UserRegistrationDTO result = userDetailService.loginUser(userLoginDTO);

        assertEquals(userDetails.getUserName(), result.getUserName());
    }

}

