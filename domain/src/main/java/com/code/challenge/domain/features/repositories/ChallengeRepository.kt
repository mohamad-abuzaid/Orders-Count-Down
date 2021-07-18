package com.code.challenge.domain.features.repositories

import com.code.challenge.domain.features.models.CategoryModel
import com.code.challenge.domain.features.models.IngredientModel
import com.code.challenge.domain.features.models.OrderModel
import io.reactivex.Single

interface ChallengeRepository {

  fun fetchOrders(): Single<List<OrderModel>>

  fun fetchCategories(): Single<List<CategoryModel>>

  fun fetchIngredients(categoryId: Long): Single<List<IngredientModel>>
}