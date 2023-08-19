package com.example.pokeapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokeapp.R
import com.example.pokeapp.databinding.ItemTypeBinding
import com.example.pokeapp.domain.model.Type
import com.example.pokeapp.util.Constants.Companion.typeToBackgroundResourceMap

class TypeAdapter : ListAdapter<Type, TypeAdapter.TypeViewHolder>(DiffCallback) {
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

        fun bind(type: Type) {
            val typeName = type.name
            binding.apply {
                textViewType.text = typeName
                textViewType.setBackgroundResource(getBackgroundResourceByType(typeName))
            }
        }

        private fun getBackgroundResourceByType(typeName: String): Int {
            return typeToBackgroundResourceMap[typeName] ?: R.drawable.background_normal_type
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Type>() {
            override fun areItemsTheSame(oldItem: Type, newItem: Type): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Type, newItem: Type): Boolean {
                return oldItem == newItem
            }
        }
    }
}