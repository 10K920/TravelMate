package com.example.fpauthenticationstep.MapActivities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fpauthenticationstep.GoogleWebAPI.UseGoogleAPIService
import com.example.fpauthenticationstep.MessagingActivities.NewMessagesActivity
import com.example.fpauthenticationstep.ModelsForNearby.NearByDetailJSON
import com.example.fpauthenticationstep.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_place_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

// Name, photo, and address of the place user clicked on is shown
// User can then decide to view other users planning on heading to the location
class PlaceDetailActivity : AppCompatActivity() {

    private lateinit var mapService: UseGoogleAPIService
    var curPlace: NearByDetailJSON?=null
    lateinit var newPlaceName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)


        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        mapService = GoogleMapAction.googleAPIService

        val curPlaceUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid=${GoogleMapAction.currentClick!!.place_id!!}&key=AIzaSyD_XOyqGfH5ExdHSNrffQ4URf57K1FCg-Q"

        mapService.getCurPlaceDetail(curPlaceUrl)
            .enqueue(object: Callback<NearByDetailJSON>{
                override fun onFailure(call: Call<NearByDetailJSON>, t: Throwable) {
                    Toast.makeText(this@PlaceDetailActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<NearByDetailJSON>, response: Response<NearByDetailJSON>) {
                    curPlace = response!!.body()
                    Log.d("CurPlaceDetailNameText", "name: ${curPlace!!.result!!.name}")
                    Log.d("CurPlaceDetailNameText", "address: ${curPlace!!.result!!.formatted_address}")
                    newPlaceName = curPlace!!.result!!.name.toString()
                    supportActionBar?.title = "$newPlaceName"
                    placename_Text.text = newPlaceName
                    address_Text.text = curPlace!!.result!!.formatted_address

                }

            })

        val picLoc = GoogleMapAction.currentClick!!.photos!![0].photo_reference!!

        if(GoogleMapAction.currentClick!!.photos != null){
            val curPlacePicUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&photoreference=$picLoc&key=AIzaSyD_XOyqGfH5ExdHSNrffQ4URf57K1FCg-Q"
            Picasso.with(this).load(curPlacePicUrl).into(curplace_imageView)
        }

        search_btn.setOnClickListener {
            db.collection("users").document(userId!!).update("place", newPlaceName)
            val intent = Intent(this, NewMessagesActivity::class.java)
            intent.putExtra("CURPLACE", newPlaceName)
            Log.d("SearchPlaces", "You are searching for users in: $newPlaceName")
            startActivity(intent)
        }
    }
}
