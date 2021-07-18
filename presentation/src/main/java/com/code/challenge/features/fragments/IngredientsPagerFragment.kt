package com.code.challenge.features.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.code.challenge.R
import com.code.challenge.common.base.views.BaseFragmentViewBinding
import com.code.challenge.common.di.ViewModelFactory
import com.code.challenge.common.extensions.makeSnackBar
import com.code.challenge.common.extensions.visible
import com.code.challenge.common.utils.Q
import com.code.challenge.databinding.FragmentIngredientsPagerBinding
import com.code.challenge.domain.common.Resource
import com.code.challenge.domain.common.ResourceType.LOADING
import com.code.challenge.domain.common.ResourceType.SUCCESS
import com.code.challenge.domain.features.models.IngredientModel
import com.code.challenge.features.adapters.IngredientsAdapter
import com.code.challenge.features.viewmodels.IngredientsFragmentViewModel
import com.code.challenge.utils.extensions.appComponent
import com.code.challenge.utils.extensions.showProgressWheel
import javax.inject.Inject

class IngredientsPagerFragment : BaseFragmentViewBinding<FragmentIngredientsPagerBinding>() {

  companion object {
    fun newInstance(categoryId: Long): IngredientsPagerFragment {
      val args = Bundle()
      args.putLong("category_id", categoryId)
      val fragment = IngredientsPagerFragment()
      fragment.arguments = args
      return fragment
    }
  }

  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  private val ingredientsFragmentViewModel: IngredientsFragmentViewModel by activityViewModels { viewModelFactory }

  private val profileFeedAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }
  private var categoryId: Long = 0L

  override fun onBind(inflater: LayoutInflater, container: ViewGroup?): FragmentIngredientsPagerBinding {
    appComponent.inject(this)
    return FragmentIngredientsPagerBinding.inflate(inflater, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initObservers()

    arguments?.getLong("category_id")?.let {
      categoryId = it
      fetchInitialData(it)
    }
  }

  private fun initObservers() {
    ingredientsFragmentViewModel.ingredientsListResult.observe(viewLifecycleOwner, this::ingredientsListObserver)
    ingredientsFragmentViewModel.searchTextLiveData.observe(viewLifecycleOwner, this::searchDataObserver)
  }

  private fun fetchInitialData(categoryId: Long) {
    ingredientsFragmentViewModel.fetchIngredientsList(categoryId)
  }

  private fun ingredientsListObserver(result: Resource<List<IngredientModel>>) {
    when (result.resourceType) {
      LOADING -> activity?.showProgressWheel(true)
      SUCCESS -> {
        activity?.showProgressWheel(false)
        result.data?.let { ingredients ->
          initIngredientsRecycler(ingredients)
        }
      }
      else -> {
        activity?.showProgressWheel(false)
        binding.ingredientPagerContainer.makeSnackBar(
          Q.SNACK.FAIL,
          getString(R.string.server_error_message),
          getString(R.string.dismiss)
        ) {}
      }
    }
  }

  private fun searchDataObserver(result: String) {
    if (result.isNullOrEmpty()) {
      fetchInitialData(categoryId)
    } else {

    }
  }

  private fun initIngredientsRecycler(ingredients: List<IngredientModel>) {
    if (ingredients.isEmpty()) {
      binding.rvIngredients.visible(false)
      binding.tvEmptyIngs.visible(true)
    } else {
      binding.rvIngredients.visible(true)
      binding.tvEmptyIngs.visible(false)
      fillRecyclerViewWithData(ingredients)
    }
  }

  private fun fillRecyclerViewWithData(ingredients: List<IngredientModel>) {
    binding.rvIngredients.adapter = profileFeedAdapter
    profileFeedAdapter.submitList(ingredients)
  }
}