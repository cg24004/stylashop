package com.devsoft.Stylashop.auth.dto;

import com.devsoft.Stylashop.entities.Role;
import lombok.Data;

@Data
public class RegisterDTO {
    private String correo;
    private String password;
    private Role role;
}