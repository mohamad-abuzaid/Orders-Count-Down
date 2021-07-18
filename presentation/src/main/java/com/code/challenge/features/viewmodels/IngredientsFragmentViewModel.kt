package com.code.challenge.features.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.code.challenge.common.base.BaseViewModel
import com.code.challenge.common.extensions.toResource
import com.code.challenge.common.schedulers.qualifires.Background
import com.code.challenge.common.schedulers.qualifires.ForeGround
import com.code.challenge.domain.common.Resource
import com.code.challenge.domain.features.models.CategoryModel
import com.code.challenge.domain.features.models.IngredientModel
import com.code.challenge.domain.features.use_case.FetchCategoriesUseCase
import com.code.challenge.domain.features.use_case.FetchIngredientsUseCase
import io.reactivex.Scheduler
import javax.inject.Inject

class IngredientsFragmentViewModel @Inject constructor(
  private val fetchCategoriesUseCase: FetchCategoriesUseCase,
  private val fetchIngredientsUseCase: FetchIngredientsUseCase,
  @Background private val backgroundScheduler: Scheduler,
  @ForeGround private val foregroundScheduler: Scheduler,
) : BaseViewModel() {

  private val _categoriesListResult = MutableLiveData<Resource<List<CategoryModel>>>()
  val categoriesListResult: LiveData<Resource<List<CategoryModel>>> get() = _categoriesListResult

  private val _ingredientsListResult = MutableLiveData<Resource<List<IngredientModel>>>()
  val ingredientsListResult: LiveData<Resource<List<IngredientModel>>> get() = _ingredientsListResult

  fun fetchCategoriesList() {
    add(
      fetchCategoriesUseCase()
        .subscribeOn(backgroundScheduler)
        .observeOn(foregroundScheduler)
        .doOnSubscribe { _categoriesListResult.value = Resource.loading() }
        .subscribe(
          {
            _categoriesListResult.value = Resource.success(it)
          },
          {
            _categoriesListResult.value = it.toResource()
          }
        )
    )
  }

  fun fetchIngredientsList(categoryId: Long) {
    add(
      fetchIngredientsUseCase(categoryId)
        .subscribeOn(backgroundScheduler)
        .observeOn(foregroundScheduler)
        .doOnSubscribe { _ingredientsListResult.value = Resource.loading() }
        .subscribe(
          {
            _ingredientsListResult.value = Resource.success(it)
          },
          {
            _ingredientsListResult.value = it.toResource()
          }
        )
    )
  }
}