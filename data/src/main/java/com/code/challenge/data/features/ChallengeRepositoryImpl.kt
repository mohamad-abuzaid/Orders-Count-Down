package com.code.challenge.data.features

import com.code.challenge.data.features.datasource.ChallengeApiService
import com.code.challenge.data.features.mappers.mapToOrderModelList
import com.code.challenge.domain.features.models.OrderModel
import com.code.challenge.domain.features.repositories.ChallengeRepository
import io.reactivex.Single
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
  private val challengeService: ChallengeApiService,
) : ChallengeRepository {

  override fun fetchOrders(): Single<List<OrderModel>> =
    challengeService.fetchOrders().map { response ->
      response.data.mapToOrderModelList()
    }
}