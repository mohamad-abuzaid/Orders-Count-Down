package com.code.challenge.domain.features.use_case

import com.code.challenge.domain.features.models.CategoryModel
import com.code.challenge.domain.features.repositories.ChallengeRepository
import io.reactivex.Single
import javax.inject.Inject

class FetchCategoriesUseCase @Inject constructor(
  private val challengeRepository: ChallengeRepository,
) {

  operator fun invoke(): Single<List<CategoryModel>> = challengeRepository.fetchCategories()
}