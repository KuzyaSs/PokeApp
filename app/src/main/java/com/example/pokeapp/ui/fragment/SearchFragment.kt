package com.example.pokeapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokeapp.PokeApplication
import com.example.pokeapp.data.repository.PokeRepository
import com.example.pokeapp.databinding.FragmentSearchBinding
import com.example.pokeapp.ui.adapter.PokemonAdapter
import com.example.pokeapp.ui.other.PokemonScrollListener
import com.example.pokeapp.ui.viewModel.PokeViewModel
import com.example.pokeapp.ui.viewModel.PokeViewModelFactory
import com.example.pokeapp.util.Constants.Companion.SPAN_COUNT
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

    private lateinit var pokemonAdapter: PokemonAdapter

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

        binding.apply {
            textInputLayoutSearch.setEndIconOnClickListener {
                binding.textInputEditTextSearch.text?.clear()
            }

            buttonTryAgain.setOnClickListener {
                viewModel.getPokemonList()
                hideError()
            }
        }

        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            when (pokemonList) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideError()
                    pokemonAdapter.submitList(pokemonList.data?.toList())
                }

                is Resource.Error -> {
                    hideProgressBar()
                    pokemonAdapter.submitList(pokemonList.data?.toList())

                    if (pokemonList.data?.isEmpty() != false) {
                        pokemonList.message?.let { errorMessage ->
                            showError(errorMessage)
                        }
                    } else {
                        if (pokemonList.message?.isNotEmpty() != false)
                            Toast.makeText(context, pokemonList.message, Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun recyclerViewSetup() {
        pokemonAdapter = PokemonAdapter { pokemon ->
            try {
                val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(pokemon)
                findNavController().navigate(action)
            } catch (_: Throwable) {
            }
        }

        binding.recyclerViewPokemonList.apply {
            adapter = pokemonAdapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
            addOnScrollListener(PokemonScrollListener {
                if (!viewModel.isLastPage()) {
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

    private fun showError(errorMessage: String) {
        binding.apply {
            imageViewErrorIcon.isVisible = true
            textViewErrorMessage.isVisible = true
            buttonTryAgain.isVisible = true

            textViewErrorMessage.text = errorMessage
        }
    }

    private fun hideError() {
        binding.apply {
            imageViewErrorIcon.isVisible = false
            textViewErrorMessage.isVisible = false
            buttonTryAgain.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.resetErrorMessage()
    }
}