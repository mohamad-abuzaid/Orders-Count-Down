package com.code.challenge.domain.features

import com.code.challenge.domain.features.models.OrderModel
import com.code.challenge.domain.features.repositories.ChallengeRepository
import com.code.challenge.domain.features.use_case.FetchOrdersUseCase
import com.code.challenge.domain.features.utils.RxImmediateSchedulerRule
import com.google.gson.JsonObject
import com.nhaarman.mockito_kotlin.any
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchOrdersUseCaseTest {

  @get:Rule var rxImmediateSchedulerRule = RxImmediateSchedulerRule()

  @Mock lateinit var challengeRepository: ChallengeRepository
  private val currListObject = Mockito.mock(JsonObject::class.java)

  private val orderObject1 = Mockito.mock(OrderModel::class.java)
  private val orderObject2 = Mockito.mock(OrderModel::class.java)
  private val ordersList = listOf(orderObject1, orderObject2)

  @Test
  fun `FetchOrdersUseCase invoke() will call fetchOrders() return list of OrderModel`() {
    // act
    Mockito.`when`(challengeRepository.fetchOrders())
      .thenReturn(Single.just(ordersList))

    val useCase = FetchOrdersUseCase(challengeRepository)
    val resultObserver = useCase().test()

    // assert
    resultObserver.assertValue(ordersList)
    resultObserver.dispose()
  }
}