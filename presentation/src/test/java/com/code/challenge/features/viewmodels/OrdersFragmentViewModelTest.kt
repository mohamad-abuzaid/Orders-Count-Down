package com.code.challenge.features.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.code.challenge.domain.common.Resource
import com.code.challenge.domain.common.ResourceType
import com.code.challenge.domain.exceptions.ChallengeCustomException
import com.code.challenge.domain.exceptions.CodeError
import com.code.challenge.domain.features.models.OrderModel
import com.code.challenge.domain.features.use_case.FetchOrdersUseCase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.internal.verification.Times
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OrdersFragmentViewModelTest {
  @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

  @Mock lateinit var fetchOrdersUseCase: FetchOrdersUseCase

  @Mock lateinit var ordersListLiveData: Observer<Resource<List<OrderModel>>>

  @Captor lateinit var ordersListCaptor: ArgumentCaptor<Resource<List<OrderModel>>>

  private lateinit var viewModel: OrdersFragmentViewModel

  private val orderObject1 = Mockito.mock(OrderModel::class.java)
  private val orderObject2 = Mockito.mock(OrderModel::class.java)
  private val ordersList = listOf(orderObject1, orderObject2)

  @Before
  fun setUp() {
    viewModel = OrdersFragmentViewModel(
      fetchOrdersUseCase,
      Schedulers.trampoline(),
      Schedulers.trampoline()
    )
  }

  @After
  fun tearDown() {
    viewModel.ordersListResult.removeObserver(ordersListLiveData)
  }

  @Test
  fun `fetchOrdersList() will return Success`() {

    viewModel.ordersListResult.observeForever(ordersListLiveData)

    // act
    Mockito.lenient().`when`(fetchOrdersUseCase())
      .thenReturn(Single.just(ordersList))

    viewModel.fetchOrdersList()

    // assert

    // Loading and Success emission
    Mockito.verify(ordersListLiveData, Times(2)).onChanged(ordersListCaptor.capture())

    val values = ordersListCaptor.allValues
    Assert.assertEquals(ResourceType.SUCCESS, values[1].resourceType)
  }

  @Test
  fun `fetchOrdersList() will return Error`() {

    viewModel.ordersListResult.observeForever(ordersListLiveData)
    val exception = ChallengeCustomException()
    exception.error = CodeError(105, "error")

    // act
    Mockito.lenient().`when`(fetchOrdersUseCase())
      .thenReturn(Single.error(exception))

    viewModel.fetchOrdersList()

    // assert

    // Loading and Success emission
    Mockito.verify(ordersListLiveData, Times(2)).onChanged(ordersListCaptor.capture())

    val values = ordersListCaptor.allValues
    Assert.assertEquals(ResourceType.ERROR, values[1].resourceType)
  }
}