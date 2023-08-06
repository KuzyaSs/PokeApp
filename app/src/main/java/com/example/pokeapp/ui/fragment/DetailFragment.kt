package com.example.pokeapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pokeapp.PokeApplication
import com.example.pokeapp.R
import com.example.pokeapp.data.remote.model.PokemonDetail
import com.example.pokeapp.repository.PokeRepository
import com.example.pokeapp.databinding.FragmentDetailBinding
import com.example.pokeapp.ui.adapter.TypeAdapter
import com.example.pokeapp.ui.viewModel.PokeViewModel
import com.example.pokeapp.ui.viewModel.PokeViewModelFactory
import com.example.pokeapp.util.Resource
import javax.inject.Inject

class DetailFragment : Fragment() {
    private val arguments: DetailFragmentArgs by navArgs()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokeViewModel by activityViewModels {
        PokeViewModelFactory(pokeRepository)
    }

    @Inject
    lateinit var pokeRepository: PokeRepository

    private lateinit var typeAdapter: TypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.applicationContext as PokeApplication).applicationComponent.inject(this)
        viewModel.setBottomNavigationViewVisibility(false)

        setUpRecyclerView()
        setUpListeners()
        setUpObservers()
    }

    private fun setUpRecyclerView() {
        typeAdapter = TypeAdapter()

        binding.recyclerViewTypeList.apply {
            adapter = typeAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpListeners() {
        binding.apply {
            imageViewBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            imageViewIsFavourite.setOnClickListener {
                viewModel.setIsFavourite(arguments.pokemon)
            }

            buttonTryAgain.setOnClickListener {
                viewModel.setPokemonDetail(arguments.pokemon.name)
            }
        }
    }

    private fun setUpObservers() {
        viewModel.pokemonDetail.observe(viewLifecycleOwner) { pokemonDetailResource ->
            when (pokemonDetailResource) {
                is Resource.Success -> {
                    hideProgressBar()
                    pokemonDetailResource.data?.let { pokemonDetail ->
                        setUpPokemonDetail(pokemonDetail)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    pokemonDetailResource.message?.let { errorMessage ->
                        showError(errorMessage)
                    }
                }

                is Resource.Loading -> {
                    hideError()
                    showProgressBar()
                }
            }
        }
    }

    private fun setUpPokemonDetail(pokemonDetail: PokemonDetail) {
        typeAdapter.submitList(pokemonDetail.types.map { type -> type.type })
        binding.apply {
            textViewId.text = getString(R.string.id, pokemonDetail.id.toString())
            textViewName.text = pokemonDetail.name.replaceFirstChar { firstChar ->
                firstChar.uppercaseChar()
            }
            textViewHpValue.text = pokemonDetail.stats[0].base_stat.toString()
            textViewAttackValue.text = pokemonDetail.stats[1].base_stat.toString()
            textViewDefenseValue.text = pokemonDetail.stats[2].base_stat.toString()
            textViewSpAtkValue.text = pokemonDetail.stats[3].base_stat.toString()
            textViewSpDefValue.text = pokemonDetail.stats[4].base_stat.toString()
            textViewSpeedValue.text = pokemonDetail.stats[5].base_stat.toString()
            Glide.with(binding.root)
                .load(pokemonDetail.sprites.front_default)
                .placeholder(R.drawable.anim_loading)
                .into(imageViewSprite)

            if (pokemonDetail.isFavourite) {
                imageViewIsFavourite.setImageResource(R.drawable.ic_favourite)
            } else {
                imageViewIsFavourite.setImageResource(R.drawable.ic_unfavourite)
            }
        }
        showStats()
    }

    private fun showStats() {
        binding.apply {
            linearLayoutStats.isVisible = true
            imageViewIsFavourite.isVisible = true
        }
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

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.setBottomNavigationViewVisibility(true)
    }
}