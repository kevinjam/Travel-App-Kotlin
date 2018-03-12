package com.thinkdevs.travelkotlin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

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
		
		mMap.setOnMapLongClickListener(mylisterner)
		//get a user location
		locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
		locationListener = object :LocationListener{
			override fun onLocationChanged(location: Location?) {
				//when a user change her location
				if (location != null) {
					mMap.clear()
					
					val userLocation = LatLng(location.latitude, location.longitude)
					mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
					
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
			
			val mintent = intent
			val info = mintent.getStringExtra("info")
			println("UUUU $intent")
			
			if (info.equals("new") ){
				mMap.clear()
				val lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
				val lastUserLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15f))
				
			}else{
				println("-------INSIDE ELSE-------- ")
				mMap.clear()
				val latitude = intent.getDoubleExtra("latitude", 0.0)
				val longitude = intent.getDoubleExtra("longitude", 0.0)
				val name = intent.getStringExtra("name")
				println("NAME NOW $name")
				val location = LatLng(latitude, longitude)
				mMap.addMarker(MarkerOptions().position(location).title(name))
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
				
				
				
			}
		}
		
	}
	
	val mylisterner = object: GoogleMap.OnMapLongClickListener {
		override fun onMapLongClick(p0: LatLng?) {
			val geocoder =Geocoder(applicationContext, Locale.getDefault())
			var address =""
			try {
				val addresslist = geocoder.getFromLocation(p0!!.latitude, p0.longitude, 1)
				if (addresslist != null && addresslist.count() > 0){
					if (addresslist[0].thoroughfare != null){
						address +=addresslist[0].thoroughfare
						if (addresslist[0].subThoroughfare != null){
							address+= addresslist[0].subThoroughfare
						}
					}
				}else{
					address = "new Place"
				}
			}catch (e:Exception){
			
			}
			
			
			mMap.addMarker(MarkerOptions().position(p0!!).title(address))
			nameArray.add(address)
			locationArray.add(p0)
			
			Toast.makeText(applicationContext, "new Place Created", Toast.LENGTH_LONG).show()
			/**
			 * Save into a Database
			 */
			try {
//				//what to save
				val latitude = p0.latitude.toString()
				val longitude = p0.longitude.toString()
				//convert into a string
				
//				val coord1 = latitude!!.toString()
//				val coord2 = longitude!!.toString()
				
				val database = openOrCreateDatabase("Places", Context.MODE_PRIVATE, null)
				database.execSQL("CREATE TABLE IF NOT EXISTS places (name VARCHAR, latitude VARCHAR, longitude VARCHAR)")
				
				val toCompile = "INSERT INTO places (name, latitude, longitude) VALUES (?, ?, ?)"
				
				val sqLiteStatement = database.compileStatement(toCompile)
				
				sqLiteStatement.bindString(1, address)
				sqLiteStatement.bindString(2, latitude)
				sqLiteStatement.bindString(3, longitude)
				
				//excute
				sqLiteStatement.execute()
				
				
			} catch (e: Exception) {
				e.printStackTrace()
			}
			
		}
		
		
	}
	
	
	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		
		if (grantResults.size > 0) {
			
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
				
				locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
			}
			
		}
		
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}
	
}
