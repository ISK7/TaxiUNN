package com.example.registrationtemplate.responses;

public class error_reg {
    String[] email;
    String[] password;
    String[] name;

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
    public String getFirstNameErr() {
        try {
            return password[0];
        }catch (Error er) {
            return "";
        }
    }
}
