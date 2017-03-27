package com.agent.trakeye.tresbu.trakeyeagent.rest;


import com.agent.trakeye.tresbu.trakeyeagent.utils.AppConstants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tresbu on 18-Oct-16.
 */

public class ApiClient {

    public static final String BASE_URL = "http://trakeye.com/";
    public static final String WIN_BASE_URL = "https://win3.tresbu.link2cloud.net/";
    // public static final String BASE_URL="http://dev.trakeye.com/";

    static OkHttpClient httpClient1 = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder ongoing = chain.request().newBuilder();
                    ongoing.addHeader(AppConstants.acceptType, "application/json");
                    ongoing.addHeader(AppConstants.contentType, "application/json");

                    return chain.proceed(ongoing.build());
                }
            })

            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .build();
    private static Retrofit retrofit = null;
    private static Retrofit retrofitKey = null;
    private static Retrofit retrofitWinKey = null;
    private static String token_id = null;
    //  private static String BASE_URL = null;
    static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder ongoing = chain.request().newBuilder();
                    ongoing.addHeader(AppConstants.acceptType, "application/json");
                    ongoing.addHeader(AppConstants.contentType, "application/json");
                    ongoing.addHeader("Authorization", AppConstants.bearer + " " + getToken_id());

                    return chain.proceed(ongoing.build());
                }
            })
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient1)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientWithKey() {
        if (retrofitKey == null) {
            retrofitKey = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitKey;
    }


    public static String getToken_id() {
        return token_id;
    }

    public static void setToken_id(String token_id) {
        ApiClient.token_id = token_id;
    }

 /*   public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }*/
}
