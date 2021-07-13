package com.code.challenge.features.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.code.challenge.common.base.views.BaseFragmentViewBinding
import com.code.challenge.common.di.ViewModelFactory
import com.code.challenge.databinding.FragmentIngredientBinding
import com.code.challenge.features.viewmodels.OrdersFragmentViewModel
import com.code.challenge.utils.extensions.appComponent
import com.code.challenge.utils.extensions.showProgressWheel
import javax.inject.Inject

class IngredientFragment : BaseFragmentViewBinding<FragmentIngredientBinding>() {

  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  private val ordersFragmentViewModel: OrdersFragmentViewModel by viewModels { viewModelFactory }

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

  }

  override fun onDetach() {
    activity?.showProgressWheel(false)
    super.onDetach()
  }
}