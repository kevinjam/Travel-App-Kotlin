package com.thinkdevs.travelkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
	}
	
	
	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		
		menuInflater.inflate(R.menu.add_place, menu)
		return super.onCreateOptionsMenu(menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		
		when(item!!.itemId){
			R.id.add_place->{
				val intent =(Intent(this, MapsActivity::class.java))
				intent.putExtra("old", "new")
			startActivity(intent)
			}
		}
		
		return super.onOptionsItemSelected(item)
	}
	
	
	
}
