package com.code.challenge.data.features.datasource

import com.code.challenge.data.features.responses.CategoryRemote
import com.code.challenge.data.features.responses.IngredientRemote
import com.code.challenge.data.features.responses.OrderRemote
import com.code.challenge.data.utils.Endpoints.Ingredients
import com.code.challenge.data.utils.Endpoints.Orders
import com.code.challenge.data.utils.parser.CodeResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ChallengeApiService {

  @GET(Orders.List)
  fun fetchOrders(): Single<CodeResponse<List<OrderRemote>>>

  @GET(Ingredients.Categories)
  fun fetchCategories(): Single<CodeResponse<List<CategoryRemote>>>

  @GET(Ingredients.List)
  fun fetchIngredients(
    @Query("category_id") categoryId: Long,
  ): Single<CodeResponse<List<IngredientRemote>>>
}