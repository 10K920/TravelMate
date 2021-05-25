package com.example.fpauthenticationstep.MapActivities

import com.example.fpauthenticationstep.GoogleWebAPI.RetrofitURLGet
import com.example.fpauthenticationstep.GoogleWebAPI.UseGoogleAPIService
import com.example.fpauthenticationstep.ModelsForNearby.Result

// for Google Place Api usage to retrieve data about the location users decided to look at
object GoogleMapAction {
    private val GoogleApiUrl = "https://maps.googleapis.com/"

    var currentClick: Result?=null

    val googleAPIService:UseGoogleAPIService
    get()=RetrofitURLGet.getClient(GoogleApiUrl).create(UseGoogleAPIService::class.java)

}