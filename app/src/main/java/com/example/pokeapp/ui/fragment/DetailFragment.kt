package com.example.pokeapp.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pokeapp.PokeApplication
import com.example.pokeapp.R
import com.example.pokeapp.data.remote.model.PokemonDetail
import com.example.pokeapp.databinding.FragmentDetailBinding
import com.example.pokeapp.repository.PokeRepository
import com.example.pokeapp.ui.adapter.TypeAdapter
import com.example.pokeapp.ui.viewModel.PokeViewModel
import com.example.pokeapp.ui.viewModel.PokeViewModelFactory
import com.example.pokeapp.util.Constants.Companion.typeToBackgroundResourceMap
import com.example.pokeapp.util.Constants.Companion.typeToColorMap
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

    override fun onStart() {
        super.onStart()
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
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
                        setUpPokemonScreen(pokemonDetail)
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

    private fun setUpPokemonScreen(pokemonDetail: PokemonDetail) {
        val mainType = pokemonDetail.types[0].type.name.replaceFirstChar { firstChar ->
            firstChar.uppercaseChar()
        }
        setUpPokemonDetail(pokemonDetail)
        setUpProgressBar(pokemonDetail, mainType)
        setUpBackground(mainType)
        showStats()
    }

    private fun setUpPokemonDetail(pokemonDetail: PokemonDetail) {
        typeAdapter.submitList(pokemonDetail.types.map { type -> type.type })
        binding.apply {
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
    }

    private fun setUpProgressBar(pokemonDetail: PokemonDetail, mainType: String) {
        val progressBarColor = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                getColorByType(mainType)
            )
        )

        binding.apply {
            progressBarHpValue.progress = pokemonDetail.stats[0].base_stat
            progressBarAttackValue.progress = pokemonDetail.stats[1].base_stat
            progressBarDefenseValue.progress = pokemonDetail.stats[2].base_stat
            progressBarSpAtkValue.progress = pokemonDetail.stats[3].base_stat
            progressBarSpDefValue.progress = pokemonDetail.stats[4].base_stat
            progressBarSpeedValue.progress = pokemonDetail.stats[5].base_stat

            progressBarHpValue.progressTintList = progressBarColor
            progressBarAttackValue.progressTintList = progressBarColor
            progressBarDefenseValue.progressTintList = progressBarColor
            progressBarSpAtkValue.progressTintList = progressBarColor
            progressBarSpDefValue.progressTintList = progressBarColor
            progressBarSpeedValue.progressTintList = progressBarColor
        }
    }

    private fun getColorByType(typeName: String): Int {
        return typeToColorMap[typeName] ?: R.color.normal
    }

    private fun setUpBackground(mainType: String) {
        binding.constraintLayout.setBackgroundResource(getBackgroundResourceByType(mainType))
    }

    private fun getBackgroundResourceByType(typeName: String): Int {
        return typeToBackgroundResourceMap[typeName] ?: R.drawable.background_normal_type
    }

    private fun showStats() {
        binding.apply {
            linearLayoutStats.isVisible = true
            linearLayoutProgressBars.isVisible = true
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

    override fun onStop() {
        super.onStop()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.setBottomNavigationViewVisibility(true)
    }
}