package com.example.registrationtemplate.responses;

import com.google.gson.annotations.SerializedName;

public class Ans_refresh_er{
    @SerializedName("refresh")
    String[] refresh;

    public String[] getRefreshErrors() {
        return refresh;
    }

    public void setRefresh(String[] refresh) {
        this.refresh = refresh;
    }
}
