package org.stand.springbootproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private List<String> roles;
    private String timestamp;
    private boolean enabled;
    //..
}
