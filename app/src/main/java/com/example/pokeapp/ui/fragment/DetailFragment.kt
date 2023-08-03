package com.example.pokeapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.pokeapp.PokeApplication
import com.example.pokeapp.data.repository.PokeRepository
import com.example.pokeapp.databinding.FragmentDetailBinding
import com.example.pokeapp.ui.viewModel.PokeViewModel
import com.example.pokeapp.ui.viewModel.PokeViewModelFactory
import javax.inject.Inject

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokeViewModel by activityViewModels {
        PokeViewModelFactory(pokeRepository)
    }

    @Inject
    lateinit var pokeRepository: PokeRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.applicationContext as PokeApplication).applicationComponent.inject(this)
        viewModel.setBottomNavigationViewVisibility(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.setBottomNavigationViewVisibility(true)
    }
}