package com.example.registrationtemplate.generalData;

import com.example.registrationtemplate.requests.*;
import com.example.registrationtemplate.responses.default_success_ans;
import com.example.registrationtemplate.responses.get_clients_ans;
import com.example.registrationtemplate.responses.login_ans;
import com.example.registrationtemplate.responses.refresh_ans;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TaxiApi {
    @POST("clients/auth/register/verify")
    Call<default_success_ans> activateAccount(@Body reg_verify regverify);
    @POST("clients/auth/password-recovery/change")
    Call<default_success_ans> changePassword(@Body change change);

    @POST("clients/auth/login")
    Call<login_ans> logInAccount(@Body login login);
    @POST("clients/auth/password-recovery")
    Call<default_success_ans> recoverPassword(@Body recover recover);
    @POST("clients/auth/register")
    Call<default_success_ans> registerClient(@Body register register);

    @POST("clients/auth/refresh")
    Call<refresh_ans> refreshToken(@Body refresh refresh);
    @POST("clients/auth/password-recovery/verify")
    Call<default_success_ans> recoverVerify(@Body recover_verify recover_verify);
    @POST("clients/auth/password-recovery/change")
    Call<default_success_ans> recoverChange(@Body recover_change recover_change);
    @GET("clients")
    Call<default_success_ans> getClients();
    @DELETE("clients")
    Call<default_success_ans> deleteClient();
}
