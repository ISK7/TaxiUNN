package com.example.registrationtemplate.responses;

public class error_verify {
    String[] email;
    String[] verification_code;
    public String getFirstEmailErr() {
        try {
            return email[0];
        }catch (Error er) {
            return "";
        }
    }
    public String getFirstCodeErr() {
        try {
            return verification_code[0];
        }catch (Error er) {
            return "";
        }
    }
}
