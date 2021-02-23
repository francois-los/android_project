package com.example.passwordmanager.dialog;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {
    @GET("/photos")
    Call<List<ApiHashes>> getAllPhotos();
}
