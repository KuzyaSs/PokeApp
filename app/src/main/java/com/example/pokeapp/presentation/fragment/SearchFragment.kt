package com.example.pokeapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokeapp.app.PokeApplication
import com.example.pokeapp.databinding.FragmentSearchBinding
import com.example.pokeapp.domain.model.Pokemon
import com.example.pokeapp.presentation.adapter.PokemonAdapter
import com.example.pokeapp.presentation.other.PokemonScrollListener
import com.example.pokeapp.presentation.viewModel.search.SearchViewModel
import com.example.pokeapp.presentation.viewModel.search.SearchViewModelFactory
import com.example.pokeapp.util.Constants.Companion.SPAN_COUNT
import com.example.pokeapp.util.Resource
import javax.inject.Inject

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by activityViewModels { searchViewModelFactory }

    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory

    private lateinit var pokemonAdapter: PokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.applicationContext as PokeApplication).applicationComponent.inject(this)
        setUpRecyclerView()
        setUpListeners()
        setUpObservers()
    }

    private fun setUpRecyclerView() {
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
                val searchString = binding.textInputEditTextSearch.text.toString()
                if (!viewModel.isLastPokemonListPage() && searchString.isBlank()) {
                    viewModel.setPokemonList()
                }
            })
        }
    }

    private fun setUpListeners() {
        binding.apply {
            textInputLayoutSearch.setEndIconOnClickListener {
                binding.textInputEditTextSearch.text?.clear()
            }
            textInputEditTextSearch.addTextChangedListener { editable ->
                editable?.let { searchEditable ->
                    val searchString = searchEditable.toString()
                    submitFilteredPokemonList(searchString = searchString)
                }
            }
        }
    }

    private fun setUpObservers() {
        viewModel.pokemonListResult.observe(viewLifecycleOwner) { pokemonListResultResource ->
            when (pokemonListResultResource) {
                is Resource.Success, is Resource.Error -> {
                    hideProgressBar()
                    val searchString = binding.textInputEditTextSearch.text.toString()
                    submitFilteredPokemonList(searchString = searchString)
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun submitFilteredPokemonList(searchString: String) {
        val filteredPokemonList = viewModel.getFilteredPokemonList(searchString = searchString)
        pokemonAdapter.submitList(filteredPokemonList)
        handleMessage(filteredPokemonList)
    }

    private fun handleMessage(pokemonList: List<Pokemon>) {
        if (pokemonList.isEmpty()) {
            showMessage()
        } else {
            hideMessage()
        }
    }

    private fun showMessage() {
        binding.apply {
            imageViewEmptyIcon.isVisible = true
            textViewMessage.isVisible = true
        }
    }

    private fun hideMessage() {
        binding.apply {
            imageViewEmptyIcon.isVisible = false
            textViewMessage.isVisible = false
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