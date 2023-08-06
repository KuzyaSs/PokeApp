package com.example.pokeapp.ui.fragment

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
import com.example.pokeapp.PokeApplication
import com.example.pokeapp.data.database.model.Pokemon
import com.example.pokeapp.repository.PokeRepository
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
            viewModel.setPokemonDetail(pokemon.name)
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
                if (!viewModel.isLastPokemonListPage() && searchString.isEmpty()) {
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
                editable?.let {
                    val searchString = editable.toString()
                    setPokemonList(searchString)
                }
            }

            buttonTryAgain.setOnClickListener {
                viewModel.setPokemonList()
            }
        }
    }

    private fun setUpObservers() {
        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemonListResource ->
            when (pokemonListResource) {
                is Resource.Success -> {
                    hideProgressBar()
                    val searchString = binding.textInputEditTextSearch.text.toString()
                    setPokemonList(searchString)
                }

                is Resource.Error -> {
                    hideProgressBar()
                    setErrorPokemonList()
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun setPokemonList(searchString: String) {
        val pokemonList = viewModel.getPokemonList(searchString)
        pokemonAdapter.submitList(pokemonList.toList())
        handleMessage(pokemonList)
    }

    private fun setErrorPokemonList() {
        val errorPokemonList = viewModel.getErrorPokemonList()
        pokemonAdapter.submitList(errorPokemonList)
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