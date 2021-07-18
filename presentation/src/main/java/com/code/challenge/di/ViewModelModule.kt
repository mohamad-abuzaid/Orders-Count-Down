package com.code.challenge.di

import androidx.lifecycle.ViewModel
import com.code.challenge.common.di.annotaions.ViewModelKey
import com.code.challenge.features.viewmodels.IngredientsFragmentViewModel
import com.code.challenge.features.viewmodels.OrdersFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(OrdersFragmentViewModel::class)
  abstract fun bindOrdersFragmentViewModel(viewModel: OrdersFragmentViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(IngredientsFragmentViewModel::class)
  abstract fun bindIngredientsFragmentViewModel(viewModel: IngredientsFragmentViewModel): ViewModel

}