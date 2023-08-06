package com.example.pokeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokeapp.R
import com.example.pokeapp.data.database.model.Pokemon
import com.example.pokeapp.databinding.ItemPokemonBinding

class PokemonAdapter(private val onItemClicked: (Pokemon) -> Unit) :
    ListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            ItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PokemonViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemon: Pokemon) {
            if (pokemon.id > 0) {
                setPokemon(pokemon)
            } else {
                setErrorPokemon(pokemon)
            }
        }

        private fun setPokemon(pokemon: Pokemon) {
            binding.apply {
                Glide.with(binding.root)
                    .load(pokemon.imageUrl)
                    .placeholder(R.drawable.anim_loading)
                    .into(imageViewSprite)

                textViewName.text = pokemon.name.replaceFirstChar { firstChar ->
                    firstChar.uppercaseChar()
                }

                root.setOnClickListener {
                    onItemClicked(pokemon)
                }
            }
        }

        private fun setErrorPokemon(pokemon: Pokemon) {
            binding.apply {
                imageViewSprite.setImageResource(R.drawable.ic_error)
                textViewName.text = pokemon.name
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem == newItem
            }
        }
    }
}