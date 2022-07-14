package com.example.gomario

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.gomario.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        loadPockemon()
    }

    var ACCESSLOCATION = 123
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESSLOCATION
                )
                return
            }
        }
        GetUserLocation()

    }

    fun GetUserLocation() {
        Toast.makeText(this, "User Location Accessed", Toast.LENGTH_LONG).show()
        //TODO: Will implement later

        var myLocation = Mylocation()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)
        var mythread = myThread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ACCESSLOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetUserLocation()
                } else {
                    Toast.makeText(this, "LOCATION ACCESS DENIED", Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


    }


    var location: Location? = null
//Get User Location..........

    inner class Mylocation : LocationListener {


        constructor() {
            location = Location("Start")
            location!!.latitude = 0.0
            location!!.longitude = 0.0
        }


        override fun onLocationChanged(location: Location) {

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            //TODO:not implemented yet
        }

        override fun onProviderEnabled(provider: String) {
            // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }


    var oldLocation: Location? = null

    inner class myThread : Thread {
        constructor() : super() {
            oldLocation = Location("Start")
            oldLocation!!.latitude = 0.0
            oldLocation!!.longitude = 0.0
        }

        override fun run() {

            while (true) {
                try {
                    //thread can never communicate with ui threfore we have used an anothor runOnUithread method to solve this issue..

                    if (oldLocation!!.distanceTo(location) == 0f) {
                        continue
                    }
                    oldLocation = location

                    runOnUiThread {
                        mMap.clear()
                        // Add a marker in Sydney and move the camera

                        //show me-------------------
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(sydney)
                                .title("PRINCE")
                                .snippet("here is my location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))


                        //show pockemon
                        for (i in 0 until ListOfPockemon.size) {
                            var newPockemon = ListOfPockemon[i]
                            if (newPockemon.isCatch == false) {


                                val pockemonLocation = LatLng(newPockemon.location!!.latitude, newPockemon.location!!.longitude)
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(pockemonLocation)
                                        .title(newPockemon.name!!)
                                        .snippet(newPockemon.des!! +", power ="+newPockemon.power!!)
                                        .icon(BitmapDescriptorFactory.fromResource(newPockemon.image!!)))

                                if(location!!.distanceTo(newPockemon.location)<2){
                                    newPockemon.isCatch=true
                                    ListOfPockemon[i]=newPockemon
                                    PlayerPower+= newPockemon.power!!
                                    Toast.makeText(applicationContext,
                                        "you caught one pockemon, your new power is "+ PlayerPower,
                                        Toast.LENGTH_LONG).show()
                                }


                            }
                        }

                    }
                    Thread.sleep(1000)
                } catch (ex: Exception) {
                }
            }
        }
    }

    var PlayerPower=0.0
    var ListOfPockemon = ArrayList<Pokemon>()

    fun loadPockemon() {
        ListOfPockemon.add(
            Pokemon(
                "Bulbasaur", "i am from JAPAN", 55.5, R.drawable.bulbasaur,
                37.772687436, -122.4026726547
            )
        )
        ListOfPockemon.add(
            Pokemon(
                "Charmander", "i am from INDIA", 504.5, R.drawable.charmander,
                37.782687436, -122.4526726547
            )
        )
        ListOfPockemon.add(
            Pokemon(
                "Squirtle", "i am from CHINA", 106.0, R.drawable.squirtle,
                37.792687436, -122.4926726547
            )
        )
    }
}


