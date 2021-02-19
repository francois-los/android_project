package com.example.passwordmanager.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static String prefix;
    private static String base_url = "https://api.pwnedpasswords.com/range/";

    public static String getPrefix(){
        return prefix;
    }
    public static void setPrefix(String p){
        prefix = p;
    }
    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder().
                    baseUrl(base_url+getPrefix()).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();
        }
        return retrofit;
    }
}
