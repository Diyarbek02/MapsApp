package com.diyarbekdev.mapsappforexam

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.Instrumentation.ActivityResult
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.diyarbekdev.mapsappforexam.databinding.ActivityCreateMapBinding
import com.diyarbekdev.mapsappforexam.models.Place
import com.diyarbekdev.mapsappforexam.models.UserMap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar

private const val TAG = "Create map Activity"
class CreateMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCreateMapBinding
    private var markers: MutableList<Marker> = mutableListOf()
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = intent.getStringExtra(EXTRA_MAP_TITLE)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment.view?.let {
            Snackbar.make(it, "Belgilew ushin uzaq basip turin`!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Ok", {})
                .setActionTextColor(ContextCompat.getColor(this, android.R.color.white))
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.miSave) {
            Log.i(TAG, "Tapped on save!")

            if (markers.isEmpty()) {
                Toast.makeText(this, "Saqlaw ushin kartadan bir jerdi belgilewin`iz kerek!", Toast.LENGTH_LONG).show()
                return true
            }
            val places =
            markers.map {marker -> Place(marker.title.toString(),
                marker.snippet.toString(), marker.position.latitude, marker.position.longitude)}
            val userMap = intent.getStringExtra(EXTRA_MAP_TITLE)?.let { UserMap(it, places) }
            val data = Intent()
            data.putExtra(EXTRA_USER_MAP, userMap)
            setResult(Activity.RESULT_OK, data)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()

        mMap.setOnInfoWindowClickListener { markerToDelete ->
            Log.i(TAG, "onWindowClickListener - delete this marker")
            markers.remove(markerToDelete)
            markerToDelete.remove()
        }

        mMap.setOnMapLongClickListener { latLng->
            Log.i(TAG, "onMapLongClickListener")
            showAlertDialog(latLng)
        }

//        // Add a marker in Sydney and move the camera
//        val siliconValley = LatLng(42.26542, 59.34036)
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(siliconValley, 15f))
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }

    private fun showAlertDialog(latLng: LatLng) {
        val placeFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_place, null)
        val dialog =
            AlertDialog.Builder(this)
            .setTitle("Jan`a adress qosiw").setView(placeFormView)
            .setNegativeButton("Biykarkaw", null)
            .setPositiveButton("Saqlaw", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = placeFormView.findViewById<EditText>(R.id.etTitle).text.toString()
            val description = placeFormView.findViewById<EditText>(R.id.etDescription).text.toString()
            if (title.trim().isEmpty() || description.trim().isEmpty()) {
                Toast.makeText(this, "Saqlaw ushin joqaridagi qatarlardi toltirin`!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val marker = mMap.addMarker(MarkerOptions().position(latLng).title(title).snippet(description))
            markers.add(marker!!)
            dialog.dismiss()
        }
    }

    override fun onMarkerClick(p0: Marker) = false
}