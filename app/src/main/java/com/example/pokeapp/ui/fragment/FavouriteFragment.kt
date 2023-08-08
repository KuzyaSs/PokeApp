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
import com.example.pokeapp.R
import com.example.pokeapp.data.database.model.Pokemon
import com.example.pokeapp.databinding.FragmentFavouriteBinding
import com.example.pokeapp.repository.PokeRepository
import com.example.pokeapp.ui.adapter.PokemonAdapter
import com.example.pokeapp.ui.viewModel.PokeViewModel
import com.example.pokeapp.ui.viewModel.PokeViewModelFactory
import com.example.pokeapp.util.Constants.Companion.SPAN_COUNT
import javax.inject.Inject

class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
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
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
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
                val action = FavouriteFragmentDirections.actionFavouriteFragmentToDetailFragment(
                    pokemon
                )
                findNavController().navigate(action)
            } catch (_: Throwable) { }
        }
        binding.recyclerViewPokemonList.apply {
            adapter = pokemonAdapter
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
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
                    setFavouritePokemonList(searchString)
                }
            }
        }
    }

    private fun setUpObservers() {
        viewModel.favouritePokemonList.observe(viewLifecycleOwner) {
            val searchString = binding.textInputEditTextSearch.text.toString()
            setFavouritePokemonList(searchString)
        }
    }

    private fun setFavouritePokemonList(searchString: String) {
        val pokemonList = viewModel.getFavouritePokemonList(searchString)
        pokemonAdapter.submitList(pokemonList)
        handleMessage(pokemonList, searchString)
    }

    private fun handleMessage(pokemonList: List<Pokemon>, searchString: String) {
        if (pokemonList.isEmpty()) {
            showMessage(searchString)
        } else {
            hideMessage()
        }
    }

    private fun showMessage(searchString: String) {
        binding.apply {
            imageViewFavouriteIcon.isVisible = true
            textViewMessage.isVisible = true
            if (searchString.isEmpty()) {
                imageViewFavouriteIcon.setImageResource(R.drawable.ic_favourite)
                textViewMessage.text = getString(R.string.you_don_t_have_favourite_pokemon_yet)
            } else {
                imageViewFavouriteIcon.setImageResource(R.drawable.ic_no_search_results)
                textViewMessage.text = getString(R.string.no_search_results)
            }
        }
    }

    private fun hideMessage() {
        binding.apply {
            imageViewFavouriteIcon.isVisible = false
            textViewMessage.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}