package com.example.passwordmanager.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {
    @GET("")
    Call<List<ApiHashes>> getallhashes();
}
