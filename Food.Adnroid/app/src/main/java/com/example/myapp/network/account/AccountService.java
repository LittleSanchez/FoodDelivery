package com.example.myapp.network.account;

import com.example.myapp.constants.Urls;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountService {
    private static AccountService mInstance;
    private static final String BASE_URL = Urls.BASE;
    private Retrofit mRetrofit;

    public AccountService() {
        OkHttpClient.Builder client = new OkHttpClient
                .Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        this.mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static AccountService getInstance() {
        if(mInstance==null)
            mInstance=new AccountService();
        return mInstance;
    }

    public ApiAccount getJSONApi() {
        return mRetrofit.create(ApiAccount.class);
    }
}
