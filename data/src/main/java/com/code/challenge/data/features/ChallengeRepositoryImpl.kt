package com.code.challenge.data.features

import com.code.challenge.data.features.datasource.ChallengeApiService
import com.code.challenge.data.features.mappers.mapToCategoryModelList
import com.code.challenge.data.features.mappers.mapToIngredientModelList
import com.code.challenge.data.features.mappers.mapToOrderModelList
import com.code.challenge.domain.features.models.CategoryModel
import com.code.challenge.domain.features.models.IngredientModel
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

  override fun fetchCategories(): Single<List<CategoryModel>> =
    challengeService.fetchCategories().map { response ->
      response.data.mapToCategoryModelList()
    }

  override fun fetchIngredients(categoryId: Long): Single<List<IngredientModel>> =
    challengeService.fetchIngredients(categoryId).map { response ->
      response.data.mapToIngredientModelList()
    }
}