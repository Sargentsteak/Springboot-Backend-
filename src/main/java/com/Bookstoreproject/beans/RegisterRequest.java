package com.Bookstoreproject.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    public String fullname;
    public String password;
    public String email;
}
