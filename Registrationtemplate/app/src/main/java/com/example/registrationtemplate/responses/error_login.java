package com.example.registrationtemplate.responses;

import java.util.List;

public class error_login {
    String[] email;
    String[] password;

    public String getFirstEmailErr() {
        try {
            return email[0];
        }catch (Error er) {
            return "";
        }
    }
    public String getFirstPasswordErr() {
        try {
            return password[0];
        }catch (Error er) {
            return "";
        }
    }
}
