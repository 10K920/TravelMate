package com.example.fpauthenticationstep

import android.util.Log
import android.widget.Toast
import androidx.test.runner.AndroidJUnit4
import com.example.fpauthenticationstep.GoogleWebAPI.UseGoogleAPIService
import com.example.fpauthenticationstep.MapActivities.GoogleMapAction
import com.example.fpauthenticationstep.ModelsForNearby.NearByDetailJSON
import junit.framework.Assert.assertTrue
import kotlinx.android.synthetic.main.activity_place_detail.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// tests if the google place api call with a certain placeid returns the place's name correctly
@RunWith(AndroidJUnit4::class)
class UseGoogleApiServiceTest {

    private lateinit var mapService: UseGoogleAPIService
    private lateinit var curPlaceUrl: String

    @Before
    fun setup() {
        mapService = GoogleMapAction.googleAPIService
        curPlaceUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJPfHyGRd-j4AR5pqrzjn22mQ&key=AIzaSyD_XOyqGfH5ExdHSNrffQ4URf57K1FCg-Q"
    }

    @Test
    fun testApiReturnFromUrlInput() {
        var placeName: String

        mapService.getCurPlaceDetail(curPlaceUrl)
            .enqueue(object: Callback<NearByDetailJSON> {
                override fun onFailure(call: Call<NearByDetailJSON>, t: Throwable) {
                }

                override fun onResponse(call: Call<NearByDetailJSON>, response: Response<NearByDetailJSON>) {
                    placeName = response!!.body()!!.result!!.name.toString()
                    assertTrue(placeName == "The Golden Fire Hydrant")
                }
            })
    }
}