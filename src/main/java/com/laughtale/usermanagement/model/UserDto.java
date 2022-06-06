package com.laughtale.usermanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private String userName;
    private String email;
    private String password;
    private String profilePicture;
}