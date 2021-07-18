package com.code.challenge.features.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.code.challenge.common.extensions.layoutInflater
import com.code.challenge.databinding.ItemIngredientBinding
import com.code.challenge.domain.features.models.IngredientModel

class IngredientsAdapter : ListAdapter<IngredientModel, IngredientsAdapter.ViewHolder>(diffUtilItemCallback) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemIngredientBinding.inflate(parent.layoutInflater, parent, false)
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let {
      holder.bindItem(it)
    }
  }

  inner class ViewHolder(private val binding: ItemIngredientBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindItem(item: IngredientModel) {
      with(binding) {
        tvIngTitle.text = item.title
        tvIngQuantity.text = item.quantity.toString()
      }
    }
  }

  companion object {
    val diffUtilItemCallback = object : DiffUtil.ItemCallback<IngredientModel>() {
      override fun areItemsTheSame(
        oldItem: IngredientModel,
        newItem: IngredientModel,
      ): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(
        oldItem: IngredientModel,
        newItem: IngredientModel,
      ): Boolean =
        oldItem == newItem
    }
  }
}