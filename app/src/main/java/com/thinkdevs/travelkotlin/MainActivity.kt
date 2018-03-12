package com.thinkdevs.travelkotlin

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*

var nameArray = ArrayList<String>()
var locationArray = ArrayList<LatLng>()
class MainActivity : AppCompatActivity() {

	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		//get info from the db
		
		
		
	}
	
	override fun onResume() {
		try {
			val database = openOrCreateDatabase("Places", Context.MODE_PRIVATE, null)
			val cursor = database.rawQuery("SELECT * FROM places", null)
			val nameIndex = cursor.getColumnIndex("name")
			val latitudeIndex = cursor.getColumnIndex("latitude")
			val longitude = cursor.getColumnIndex("longitude")
			
			cursor.moveToFirst()
			
			nameArray.clear()
			locationArray.clear()
			while (cursor != null){
				val nameFromDatabase = cursor.getString(nameIndex)
				val latitudeFromDatabase = cursor.getString(latitudeIndex)
				val longtitudeFromDatabase = cursor.getString(longitude)
				
				nameArray.add(nameFromDatabase)
				val latitudeCoordinate = latitudeFromDatabase.toDouble()
				val longitudeCoordinate = longtitudeFromDatabase.toDouble()
				
				val location = LatLng(latitudeCoordinate, longitudeCoordinate)
				locationArray.add(location)
				cursor.moveToNext()
			}
			
		}catch (e:Exception){
		
		}
		
		val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nameArray)
		listView.adapter = arrayAdapter
		
		listView.setOnItemClickListener{adapterView, view, i, l->
			val intent = Intent(applicationContext, MapsActivity::class.java)
			intent.putExtra("info", "old")
			intent.putExtra("name", nameArray[i])
			intent.putExtra("latitude", locationArray[i].latitude)
			intent.putExtra("longitude", locationArray[i].longitude)
			startActivity(intent)
			
			
			
		}
		super.onResume()
	}
	
	
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		
		menuInflater.inflate(R.menu.add_place, menu)
		return super.onCreateOptionsMenu(menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		
		when(item!!.itemId){
			R.id.add_place->{
				val intent =(Intent(applicationContext, MapsActivity::class.java))
				intent.putExtra("info", "new")
			startActivity(intent)
			}
		}
		
		return super.onOptionsItemSelected(item)
	}
	
	
	
}
