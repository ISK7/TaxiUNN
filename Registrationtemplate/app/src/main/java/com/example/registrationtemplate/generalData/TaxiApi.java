package com.example.registrationtemplate.generalData;

import com.example.registrationtemplate.requests.*;
import com.example.registrationtemplate.responses.Ans_delete_clients;
import com.example.registrationtemplate.responses.Ans_get_clients;
import com.example.registrationtemplate.responses.Ans_login;
import com.example.registrationtemplate.responses.Ans_password_recovery;
import com.example.registrationtemplate.responses.Ans_password_recovery_change;
import com.example.registrationtemplate.responses.Ans_password_recovery_verify;
import com.example.registrationtemplate.responses.Ans_refresh;
import com.example.registrationtemplate.responses.Ans_register;
import com.example.registrationtemplate.responses.Ans_register_verify;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TaxiApi {
    @POST("clients/auth/activate")
    Call<Ans_register_verify> activateAccount(@Body reg_verify regverify);
    @POST("clients/auth/login")
    Call<Ans_login> logInAccount(@Body login login);
    @POST("clients/auth/password-recovery")
    Call<Ans_password_recovery> recoverPassword(@Body recover recover);
    @POST("clients/auth/register")
    Call<Ans_register> registerClient(@Body register register);
    @POST("clients/auth/refresh")
    Call<Ans_refresh> refreshToken(@Body refresh refresh);
    @POST("clients/auth/password-recovery/verify")
    Call<Ans_password_recovery_verify> recoverVerify(@Body recover_verify recover_verify);
    @POST("clients/auth/password-recovery/change")
    Call<Ans_password_recovery_change> recoverChange(@Body recover_change recover_change);
    @GET("clients")
    Call<Ans_get_clients> getClients();
    @DELETE("clients")
    Call<Ans_delete_clients> deleteClient();
}
