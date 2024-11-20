package com.example.registrationtemplate.responses;

public class error_recover_change {
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
