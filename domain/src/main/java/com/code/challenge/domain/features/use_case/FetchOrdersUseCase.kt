package com.code.challenge.domain.features.use_case

import com.code.challenge.domain.features.models.OrderModel
import com.code.challenge.domain.features.repositories.ChallengeRepository
import com.google.gson.JsonObject
import io.reactivex.Single
import javax.inject.Inject

class FetchOrdersUseCase @Inject constructor(
  private val challengeRepository: ChallengeRepository,
) {

  operator fun invoke(): Single<List<OrderModel>> = challengeRepository.fetchOrders()
}