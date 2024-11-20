package com.example.registrationtemplate.responses;

public class error_recovery {
    String[] email;
    public String getFirstEmailErr() {
        try {
            return email[0];
        }catch (Error er) {
            return "";
        }
    }
}
