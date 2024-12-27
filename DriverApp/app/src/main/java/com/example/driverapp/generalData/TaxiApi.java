package com.example.driverapp.generalData;

import com.example.driverapp.requests.*;
import com.example.driverapp.responses.Ans_delete_clients;
import com.example.driverapp.responses.Ans_get_clients;
import com.example.driverapp.responses.Ans_login;
import com.example.driverapp.responses.Ans_password_recovery;
import com.example.driverapp.responses.Ans_password_recovery_change;
import com.example.driverapp.responses.Ans_password_recovery_verify;
import com.example.driverapp.responses.Ans_refresh;
import com.example.driverapp.responses.Ans_register;
import com.example.driverapp.responses.Ans_register_verify;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TaxiApi {
    @POST("drivers/auth/activate")
    Call<Ans_register_verify> activateAccount(@Body reg_verify regverify);
    @POST("drivers/auth/login")
    Call<Ans_login> logInAccount(@Body login login);
    @POST("drivers/auth/password-recovery")
    Call<Ans_password_recovery> recoverPassword(@Body recover recover);
    @POST("drivers/auth/register")
    Call<Ans_register> registerClient(@Body register register);
    @POST("drivers/auth/refresh")
    Call<Ans_refresh> refreshToken(@Body refresh refresh);
    @POST("drivers/auth/password-recovery/verify")
    Call<Ans_password_recovery_verify> recoverVerify(@Body recover_verify recover_verify);
    @POST("drivers/auth/password-recovery/change")
    Call<Ans_password_recovery_change> recoverChange(@Body recover_change recover_change);
    @GET("drivers")
    Call<Ans_get_clients> getClients();
    @DELETE("drivers")
    Call<Ans_delete_clients> deleteClient();
}
