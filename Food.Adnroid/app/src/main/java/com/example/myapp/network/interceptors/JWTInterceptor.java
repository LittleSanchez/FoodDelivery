package com.example.myapp.network.interceptors;

import com.example.myapp.application.HomeApplication;
import com.example.myapp.security.JwtSecurityService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JWTInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        JwtSecurityService jwtSecurityService = HomeApplication.getInstance();
        String token = jwtSecurityService.getToken();
        Request.Builder builder = chain.request().newBuilder();
        if(token != null && !token.isEmpty())
            builder.header("Authorization", "Bearer " + token);

        return chain.proceed(builder.build());
    }
}
