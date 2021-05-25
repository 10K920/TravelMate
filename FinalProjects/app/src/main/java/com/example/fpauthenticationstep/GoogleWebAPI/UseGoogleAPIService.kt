package com.example.fpauthenticationstep.GoogleWebAPI

import com.example.fpauthenticationstep.ModelsForNearby.NearByDetailJSON
import com.example.fpauthenticationstep.ModelsForNearby.RootObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

// interface to keep track of what type of url is being called
interface UseGoogleAPIService {
    @GET
    fun getNearbyPlaces(@Url url:String): Call<RootObject>

    @GET
    fun getCurPlaceDetail(@Url url:String): Call<NearByDetailJSON>
}