package com.thinkdevs.travelkotlin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
	
	private lateinit var mMap: GoogleMap
	 var locationManager: LocationManager?= null
	var locationListener:LocationListener? = null
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_maps)
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		val mapFragment = supportFragmentManager
				.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync(this)
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
		
		//get a user location
		locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
		locationListener = object :LocationListener{
			override fun onLocationChanged(location: Location?) {
				//when a user change her location
				if (location != null) {
					var userlocation = LatLng(location!!.latitude, location!!.latitude)
					mMap.addMarker(MarkerOptions().position(userlocation))
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation ,17f))
				}
			}
			
			override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
			}
			
			override fun onProviderEnabled(provider: String?) {
			}
			
			override fun onProviderDisabled(provider: String?) {
			}
			
		}
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
			
		}else{
			locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 2f, locationListener)
			val intent = intent.getStringExtra("old")
			println("UUUU $intent")
			
			if (intent.equals("new") ){
			
				mMap.clear()
				val lastlocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
				var lastUserLocation = LatLng(lastlocation.latitude, lastlocation.longitude)
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 17f))
				
			}else{
				//
			}
		}
		
	}
	
	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		if (grantResults.size > 0){
			if (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
				locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 2f, locationListener)
			}
		}
		
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}
}
