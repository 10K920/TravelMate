package com.example.fpauthenticationstep.GoogleWebAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// retrofit2 for retrieving google place api data formatted as json
object RetrofitURLGet {
    private var retrofit: Retrofit?=null

    fun getClient(url: String): Retrofit{
        if(retrofit==null){
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}