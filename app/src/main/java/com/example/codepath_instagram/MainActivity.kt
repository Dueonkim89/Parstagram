package com.example.codepath_instagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.codepath_instagram.fragments.ComposeFragment
import com.example.codepath_instagram.fragments.FeedFragment
import com.example.codepath_instagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        // event handler for each item in bottom nav bar
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener{
            item ->

            var fragmentToShow: Fragment? = null
            when (item.itemId) {

                // navigate to home screen
                R.id.action_home -> {
                    fragmentToShow = FeedFragment()
                }

                // navigate to compose screen
                R.id.action_compose -> {
                    fragmentToShow = ComposeFragment()
                }

                // navigate to profile screen
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }

            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }

            true
        }

        // Set default selection
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home
        //queryPosts()
    }



}