package com.example.registrationtemplate.responses;

public class error_default_password {
    String[] password;

    public String getFirstPasswordErr() {
        try {
            return password[0];
        }catch (Error er) {
            return "";
        }
    }
}
