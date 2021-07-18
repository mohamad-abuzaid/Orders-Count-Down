package com.code.challenge.features.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.code.challenge.R
import com.code.challenge.common.base.views.BaseFragmentViewBinding
import com.code.challenge.common.di.ViewModelFactory
import com.code.challenge.common.extensions.makeSnackBar
import com.code.challenge.common.utils.Q
import com.code.challenge.databinding.FragmentIngredientBinding
import com.code.challenge.domain.common.Resource
import com.code.challenge.domain.common.ResourceType.LOADING
import com.code.challenge.domain.common.ResourceType.SUCCESS
import com.code.challenge.domain.features.models.CategoryModel
import com.code.challenge.features.adapters.IngredientsPagerAdapter
import com.code.challenge.features.viewmodels.IngredientsFragmentViewModel
import com.code.challenge.utils.extensions.appComponent
import com.code.challenge.utils.extensions.showProgressWheel
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class IngredientFragment : BaseFragmentViewBinding<FragmentIngredientBinding>() {

  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  private val ingredientsFragmentViewModel: IngredientsFragmentViewModel by viewModels { viewModelFactory }

  override fun onBind(
    inflater: LayoutInflater,
    container: ViewGroup?,
  ): FragmentIngredientBinding {
    appComponent.inject(this)
    return FragmentIngredientBinding.inflate(inflater, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    super.onViewCreated(view, savedInstanceState)

    initObservers()

    fetchInitialData()
  }

  private fun initObservers() {
    ingredientsFragmentViewModel.categoriesListResult.observe(viewLifecycleOwner, this::categoriesListObserver)
  }

  private fun fetchInitialData() {
    ingredientsFragmentViewModel.fetchCategoriesList()
  }

  private fun categoriesListObserver(result: Resource<List<CategoryModel>>) {
    when (result.resourceType) {
      LOADING -> activity?.showProgressWheel(true)
      SUCCESS -> {
        activity?.showProgressWheel(false)
        result.data?.let { categories ->
          setupViewPager(ArrayList(categories))
        }
      }
      else -> {
        activity?.showProgressWheel(false)
        binding.ingredientContainer.makeSnackBar(
          Q.SNACK.FAIL,
          getString(R.string.server_error_message),
          getString(R.string.dismiss)
        ) {}
      }
    }
  }

  private fun setupViewPager(catList: ArrayList<CategoryModel>) {
    binding.vpIngsPager.isUserInputEnabled = false
    binding.vpIngsPager.adapter = IngredientsPagerAdapter(childFragmentManager, lifecycle, catList)
    binding.vpIngsPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
    TabLayoutMediator(binding.tabLayout, binding.vpIngsPager) { tab, position ->
      tab.text = catList[position].title
    }.attach()
  }

  override fun onDetach() {
    activity?.showProgressWheel(false)
    super.onDetach()
  }
}