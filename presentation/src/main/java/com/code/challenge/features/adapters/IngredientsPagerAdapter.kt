package com.code.challenge.features.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.code.challenge.domain.features.models.CategoryModel
import com.code.challenge.features.fragments.IngredientsPagerFragment

class IngredientsPagerAdapter(
  fm: FragmentManager,
  lifeCycle: Lifecycle,
  private val categories: ArrayList<CategoryModel>,
) : FragmentStateAdapter(fm, lifeCycle) {

  override fun getItemCount(): Int = categories.size

  override fun createFragment(position: Int): Fragment {
    return IngredientsPagerFragment.newInstance(categories[position].id)
  }
}