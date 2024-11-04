package com.spm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegistrationDTO {

    private String userName;
    private String address;
    private String email;


}
