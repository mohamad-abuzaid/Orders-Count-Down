package com.code.challenge.domain.features.repositories

import com.code.challenge.domain.features.models.OrderModel
import com.google.gson.JsonObject
import io.reactivex.Single

interface ChallengeRepository {

  fun fetchOrders(): Single<List<OrderModel>>
}