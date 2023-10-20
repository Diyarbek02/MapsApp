package com.diyarbekdev.mapsappforexam

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.diyarbekdev.mapsappforexam.databinding.ActivityDisplayMapBinding
import com.diyarbekdev.mapsappforexam.models.UserMap
import com.google.android.gms.maps.model.LatLngBounds
import java.io.Serializable

private const val TAG = "DisplayMapActivity"
class DisplayMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var userMap: UserMap
    private lateinit var binding: ActivityDisplayMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userMap = intent.getSerializableExtra(EXTRA_USER_MAP) as UserMap

        supportActionBar?.title  = userMap.title

        binding = ActivityDisplayMapBinding.inflate(layoutInflater)
     setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Log.i(TAG,"user map to render: ${userMap.title}")

        val boundsBuilder = LatLngBounds.Builder()
        for (place in userMap.places) {
            val latLng = LatLng(place.latitude, place.longitude)
            boundsBuilder.include(latLng)
            mMap.addMarker(MarkerOptions().position(latLng).title(place.title).snippet(place.description))
        }
       // mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 0))
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 0))

//        val point = LatLng(47.249912481336494, 39.69719647908453)
//        mMap.addMarker(MarkerOptions().position(point).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(point))
    }
}