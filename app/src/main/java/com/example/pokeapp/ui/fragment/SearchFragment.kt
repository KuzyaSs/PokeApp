package com.example.pokeapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.pokeapp.PokeApplication
import com.example.pokeapp.data.repository.PokeRepository
import com.example.pokeapp.databinding.FragmentSearchBinding
import com.example.pokeapp.ui.adapter.PokemonAdapter
import com.example.pokeapp.ui.other.PokemonScrollListener
import com.example.pokeapp.ui.viewModel.PokeViewModel
import com.example.pokeapp.ui.viewModel.PokeViewModelFactory
import com.example.pokeapp.util.Resource
import javax.inject.Inject

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokeViewModel by activityViewModels {
        PokeViewModelFactory(pokeRepository)
    }

    @Inject
    lateinit var pokeRepository: PokeRepository

    lateinit var pokemonAdapter: PokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.applicationContext as PokeApplication).applicationComponent.inject(this)

        recyclerViewSetup()

        binding.textInputLayoutSearch.setEndIconOnClickListener {
            binding.textInputEditTextSearch.text?.clear()
        }

        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            when (pokemonList) {
                is Resource.Success -> {
                    hideProgressBar()
                    pokemonAdapter.submitList(pokemonList.data)
                }

                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(context, pokemonList.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun recyclerViewSetup() {
        pokemonAdapter = PokemonAdapter {

        }

        binding.recyclerViewPokemonList.apply {
            adapter = pokemonAdapter
            layoutManager = GridLayoutManager(context, 2)
            addOnScrollListener(PokemonScrollListener { isAtLastItem ->
                if (isAtLastItem) {
                    viewModel.getPokemonList()
                }
            })
        }
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}