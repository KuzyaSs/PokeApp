package com.example.pokeapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.pokeapp.R
import com.example.pokeapp.databinding.ActivityPokeBinding

class PokeActivity : AppCompatActivity() {
    private var _binding: ActivityPokeBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityPokeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        setupWithNavController(bottomNavigationView, navController)
    }
}