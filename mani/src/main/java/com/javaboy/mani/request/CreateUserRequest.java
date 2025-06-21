package com.javaboy.mani.request;

import lombok.Data;
import org.hibernate.annotations.NaturalId;
@Data
public class CreateUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
