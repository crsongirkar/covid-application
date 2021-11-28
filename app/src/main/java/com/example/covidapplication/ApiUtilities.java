package com.example.covidapplication;

import com.example.covidapplication.ApiInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class ApiUtilities {

    public static Retrofit retrofit=null;
public static ApiInterface getApiInterface()
   {
       if(retrofit==null)
        {
                retrofit=new Retrofit.Builder().baseUrl(ApiInterface.BASE_URl).addConverterFactory(GsonConverterFactory.create()).build();
                }
                return retrofit.create(ApiInterface.class);

   }

}

