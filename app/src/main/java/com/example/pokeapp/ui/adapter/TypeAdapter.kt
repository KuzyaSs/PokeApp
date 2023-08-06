package com.example.pokeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokeapp.R
import com.example.pokeapp.data.remote.model.TypeX
import com.example.pokeapp.databinding.ItemTypeBinding

class TypeAdapter : ListAdapter<TypeX, TypeAdapter.TypeViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        return TypeViewHolder(
            ItemTypeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class TypeViewHolder(private val binding: ItemTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val typeToBackgroundMap = mapOf(
            "Normal" to R.drawable.background_normal_type,
            "Fire" to R.drawable.background_fire_type,
            "Water" to R.drawable.background_water_type,
            "Grass" to R.drawable.background_grass_type,
            "Electric" to R.drawable.background_electric_type,
            "Ice" to R.drawable.background_ice_type,
            "Fighting" to R.drawable.background_fighting_type,
            "Poison" to R.drawable.background_poison_type,
            "Ground" to R.drawable.background_ground_type,
            "Flying" to R.drawable.background_flying_type,
            "Psychic" to R.drawable.background_psychic_type,
            "Bug" to R.drawable.background_bug_type,
            "Rock" to R.drawable.background_rock_type,
            "Ghost" to R.drawable.background_ghost_type,
            "Dark" to R.drawable.background_dark_type,
            "Dragon" to R.drawable.background_dragon_type,
            "Steel" to R.drawable.background_steel_type,
            "Fairy" to R.drawable.background_fairy_type,
        )

        fun bind(type: TypeX) {
            val typeName = type.name.replaceFirstChar { firstChar -> firstChar.uppercaseChar() }
            binding.apply {
                textViewType.text = typeName
                textViewType.setBackgroundResource(getBackgroundResourceByType(typeName))
            }
        }

        private fun getBackgroundResourceByType(typeName: String): Int {
            return typeToBackgroundMap[typeName] ?: R.drawable.background_normal_type
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TypeX>() {
            override fun areItemsTheSame(oldItem: TypeX, newItem: TypeX): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: TypeX, newItem: TypeX): Boolean {
                return oldItem == newItem
            }
        }
    }
}