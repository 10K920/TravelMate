package com.example.fpauthenticationstep.MapActivities

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.fpauthenticationstep.GoogleWebAPI.UseGoogleAPIService
import com.example.fpauthenticationstep.ModelsForNearby.RootObject
import com.example.fpauthenticationstep.R
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Response
import java.lang.StringBuilder

// Map Activity in which users can choose types of place nearby
// when users click on a marker, details page about the place is started
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var latitude: Double=0.toDouble()
    private var longitude: Double=0.toDouble()

    private lateinit var mLastLocation: Location
    private var markersBlue: Marker?=null

    lateinit var mapService:UseGoogleAPIService
    internal lateinit var currentLoc: RootObject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    companion object {
        private const val MY_PERMISSION_CODE: Int = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapService = GoogleMapAction.googleAPIService

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()){
                buildLocationRequest()
                buildLocationCallBack()
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            }
        }
        else {
            buildLocationRequest()
            buildLocationCallBack()
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }

        bottom_navigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.action_attraction -> nearByPlace("everything")
                R.id.action_restaurant -> nearByPlace("restaurant")
            }
            true
        }

    }

    private fun nearByPlace(placeType: String) {
        mMap.clear()
        val url = setUrlNearby(latitude,longitude,placeType)

        mapService.getNearbyPlaces(url)
            .enqueue(object : retrofit2.Callback<RootObject>{

                override fun onResponse(call: Call<RootObject>?, response: Response<RootObject>?) {
                    currentLoc = response!!.body()!!


                    if (response!!.isSuccessful){
                        for(i in 0 until response!!.body()!!.results!!.size){
                            val markerOptions = MarkerOptions()
                            val placesNear = response.body()!!.results!![i]
                            val lat = placesNear.geometry!!.location!!.lat
                            val lng = placesNear.geometry!!.location!!.lng
                            val placeName = placesNear.name
                            val latLng = LatLng(lat,lng)

                            markerOptions.position(latLng)
                            markerOptions.title(placeName)

                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))


                            markerOptions.snippet(i.toString())

                            mMap.addMarker(markerOptions)
                        }


                    }
                }

                override fun onFailure(call: Call<RootObject>, t: Throwable) {
                    Toast.makeText(baseContext,"${t.message}",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun setUrlNearby(latitude: Double, longitude: Double, placeType: String): String {
        if (placeType == "everything"){
            val placesNearMeUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
            placesNearMeUrl.append("?location=$latitude,$longitude")
            placesNearMeUrl.append("&radius=40000")
            placesNearMeUrl.append("&keyword=tourist")
            placesNearMeUrl.append("&key=AIzaSyD_XOyqGfH5ExdHSNrffQ4URf57K1FCg-Q")

            Log.d("MapsActivity", "PlacesNearByURL: ${placesNearMeUrl.toString()}")
            return placesNearMeUrl.toString()
        }
        else {
            val placesNearMeUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
            placesNearMeUrl.append("?location=$latitude,$longitude")
            placesNearMeUrl.append("&radius=40000")
            placesNearMeUrl.append("&type=$placeType")
            placesNearMeUrl.append("&key=AIzaSyD_XOyqGfH5ExdHSNrffQ4URf57K1FCg-Q")

            Log.d("MapsActivity", "PlacesNearByURL: ${placesNearMeUrl.toString()}")
            return placesNearMeUrl.toString()
        }
    }

    private fun checkLocationPermission():Boolean {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )
            }
            else{
                ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )
            }
            return false
        }
        else {
            return true
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    private fun buildLocationCallBack(){
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations.get(p0!!.locations.size-1)
                if(markersBlue != null){
                    markersBlue!!.remove()
                }
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLng = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("You're here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                markersBlue = mMap!!.addMarker(markerOptions)

                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            MY_PERMISSION_CODE ->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(checkLocationPermission()){
                            buildLocationRequest()
                            buildLocationCallBack()
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                            mMap.isMyLocationEnabled=true
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap!!.isMyLocationEnabled = true
            }
        }
        else{
            mMap!!.isMyLocationEnabled = true
        }

        mMap!!.setOnMarkerClickListener { marker ->
            GoogleMapAction.currentClick = currentLoc!!.results!![Integer.parseInt(marker.snippet)]
            var intent = Intent(this@MapsActivity, PlaceDetailActivity::class.java)
            startActivity(intent)
            true
        }

        mMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.to_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.to_menu -> {
                val intent = Intent(this, com.example.fpauthenticationstep.PathActivities.Menu::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
