package com.yoeki.kalpnay.frdinventry.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {


//  public static final String BASE_URL = "http://10.10.10.241:806/api/"; //local url
public static final String BASE_URL = "http://219.90.65.215:9004/api/"; //URL for Work For Home url
//    public static final String BASE_URL = "http://192.168.10.216:806/api/"; //Site URL


    private static Retrofit retrofit = null;
    private static OkHttpClient client;

    public static Retrofit getClient() {
        //change your base URL

        if (retrofit == null) {


            client = new OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).connectTimeout(5, TimeUnit.MINUTES).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}