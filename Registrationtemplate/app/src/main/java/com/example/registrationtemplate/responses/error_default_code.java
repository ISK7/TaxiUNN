package com.example.registrationtemplate.responses;

public class error_default_code {
    String[] verification_code;
    public String getFirstCodeErr() {
        try {
            return verification_code[0];
        }catch (Error er) {
            return "";
        }
    }
}
