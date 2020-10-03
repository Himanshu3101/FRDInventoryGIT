package com.yoeki.kalpnay.frdinventry.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {


//  public static final String BASE_URL = "http://10.10.10.241:806/api/"; //local url
public static final String BASE_URL = "http://219.90.65.215:9047/api/"; //URL for Work For Home url
//    public static final String BASE_URL = "http://192.168.10.216:806/api/"; //Site URL
//  public static final String BASE_URL = "http://192.168.10.167:809/api/"; //site url Given by Rohit sir on 25-06-2020
//  public static final String BASE_URL = "http://192.168.10.216:9008/api/"; //site url Given by Rohit sir on 25-06-2020// Only for Production URL
//public static final String BASE_URL = "http://37.224.71.212:809/api/"; //saudi url 26-06-2020//for testing for us
//public static final String BASE_URL = "http://192.168.10.167:8002/api/"; //saudi url 15-Sep-2020//for testing for us
//public static final String BASE_URL = "http://37.224.71.212:8002/api/"; //saudi url 28-08-2020//for testing not for us

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