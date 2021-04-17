package com.example.myapp.network.account;

import com.example.myapp.network.account.dto.LoginDto;
import com.example.myapp.network.account.dto.LoginResultDto;
import com.example.myapp.network.account.dto.RegistrationDTO;
import com.example.myapp.network.account.dto.RegistrationResultDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiAccount {
    @POST("/api/account/login")
    public Call<LoginResultDto> login(@Body LoginDto loginDto);

    @POST("/api/account/registration")
    public Call<RegistrationResultDTO> register(@Body RegistrationDTO registrationDTO);
}
