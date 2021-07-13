package com.code.challenge.data.features.datasource

import com.code.challenge.data.features.responses.OrderRemote
import com.code.challenge.data.utils.Endpoints.Orders
import com.code.challenge.data.utils.parser.CodeResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ChallengeApiService {

  @GET(Orders.List)
  fun fetchOrders(): Single<CodeResponse<List<OrderRemote>>>
}