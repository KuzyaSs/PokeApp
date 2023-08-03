package com.example.pokeapp.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.example.pokeapp.PokeApplication
import com.example.pokeapp.R
import com.example.pokeapp.data.repository.PokeRepository
import com.example.pokeapp.databinding.ActivityPokeBinding
import com.example.pokeapp.ui.viewModel.PokeViewModel
import com.example.pokeapp.ui.viewModel.PokeViewModelFactory
import javax.inject.Inject

class PokeActivity : AppCompatActivity() {
    private var _binding: ActivityPokeBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val viewModel: PokeViewModel by viewModels {
        PokeViewModelFactory(pokeRepository)
    }

    @Inject
    lateinit var pokeRepository: PokeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPokeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (applicationContext as PokeApplication).applicationComponent.inject(this)

        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(bottomNavigationView, navController)

        viewModel.bottomNavigationViewVisibility.observe(this) { isVisible ->
            if (isVisible) {
                binding.bottomNavigationView.show()
            } else {
                binding.bottomNavigationView.hide()
            }
        }
    }

    private fun View.show() {
        if (!isVisible) {
            TransitionManager.beginDelayedTransition(this.parent as ViewGroup, Slide(Gravity.BOTTOM))
            isVisible = true
        }
    }

    private fun View.hide() {
        if (isVisible) {
            TransitionManager.beginDelayedTransition(this.parent as ViewGroup, Slide(Gravity.BOTTOM))
            isVisible = false
        }
    }
}