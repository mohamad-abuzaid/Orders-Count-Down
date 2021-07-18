package com.code.challenge.domain.features.use_case

import com.code.challenge.domain.features.models.IngredientModel
import com.code.challenge.domain.features.repositories.ChallengeRepository
import io.reactivex.Single
import javax.inject.Inject

class FetchIngredientsUseCase @Inject constructor(
  private val challengeRepository: ChallengeRepository,
) {

  operator fun invoke(categoryId: Long): Single<List<IngredientModel>> =
    challengeRepository.fetchIngredients(categoryId)
}