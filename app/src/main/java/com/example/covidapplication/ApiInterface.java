package com.example.covidapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    String BASE_URl="https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<List<ModelClass>>     getcountrydata();
}
