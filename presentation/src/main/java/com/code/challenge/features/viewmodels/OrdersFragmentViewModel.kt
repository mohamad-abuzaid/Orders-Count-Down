package com.code.challenge.features.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.code.challenge.common.base.BaseViewModel
import com.code.challenge.common.extensions.toResource
import com.code.challenge.common.schedulers.qualifires.Background
import com.code.challenge.common.schedulers.qualifires.ForeGround
import com.code.challenge.domain.common.Resource
import com.code.challenge.domain.features.models.OrderModel
import com.code.challenge.domain.features.use_case.FetchOrdersUseCase
import io.reactivex.Scheduler
import javax.inject.Inject

class OrdersFragmentViewModel @Inject constructor(
  private val fetchOrdersUseCase: FetchOrdersUseCase,
  @Background private val backgroundScheduler: Scheduler,
  @ForeGround private val foregroundScheduler: Scheduler,
) : BaseViewModel() {

  val ordersList = mutableListOf<OrderModel>()
  val openOrderWorkers = mutableListOf<Long>()

  private val _ordersListResult = MutableLiveData<Resource<List<OrderModel>>>()
  val ordersListResult: LiveData<Resource<List<OrderModel>>> get() = _ordersListResult

  fun fetchOrdersList() {
    add(
      fetchOrdersUseCase()
        .subscribeOn(backgroundScheduler)
        .observeOn(foregroundScheduler)
        .doOnSubscribe { _ordersListResult.value = Resource.loading() }
        .subscribe(
          {
            ordersList.clear()
            ordersList.addAll(it)
            _ordersListResult.value = Resource.success(ordersList)
          },
          {
            _ordersListResult.value = it.toResource()
          }
        )
    )
  }
}