package com.mieasy.securitydemo01.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    private Integer id;

    private String username;

    private String password;
}
