package com.example.pokeapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokeapp.R
import com.example.pokeapp.data.remote.model.TypeX
import com.example.pokeapp.databinding.ItemTypeBinding
import com.example.pokeapp.util.Constants.Companion.typeToBackgroundResourceMap

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

        fun bind(type: TypeX) {
            val typeName = type.name.replaceFirstChar { firstChar -> firstChar.uppercaseChar() }
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