package com.example.pokeapp.presentation.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.example.pokeapp.R
import com.example.pokeapp.app.PokeApplication
import com.example.pokeapp.databinding.ActivityPokeBinding
import com.example.pokeapp.presentation.viewModel.base.BaseViewModel
import com.example.pokeapp.presentation.viewModel.base.BaseViewModelFactory
import javax.inject.Inject

class PokeActivity : AppCompatActivity() {
    private var _binding: ActivityPokeBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    val baseViewModel: BaseViewModel by viewModels { baseViewModelFactory }

    @Inject
    lateinit var baseViewModelFactory: BaseViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPokeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as PokeApplication).applicationComponent.inject(this)
        setUpNavigation()
        setUpObservers()
    }

    private fun setUpNavigation() {
        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        setupWithNavController(bottomNavigationView, navController)
    }

    private fun setUpObservers() {
        baseViewModel.bottomNavigationViewVisibility.observe(this) { isVisible ->
            if (isVisible) {
                binding.bottomNavigationView.isVisible = true
            } else {
                binding.bottomNavigationView.hide()
            }
        }
    }

    private fun View.hide() {
        if (isVisible) {
            TransitionManager.beginDelayedTransition(
                this.parent as ViewGroup,
                Slide(Gravity.BOTTOM)
            )
            isVisible = false
        }
    }
}