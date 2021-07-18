package com.code.challenge.data.features.mappers

import com.code.challenge.data.features.responses.CategoryRemote
import com.code.challenge.data.features.responses.IngredientRemote
import com.code.challenge.domain.features.models.CategoryModel
import com.code.challenge.domain.features.models.IngredientModel

fun CategoryRemote.mapToCategoryModel(): CategoryModel {
  return CategoryModel(
    id,
    title,
  )
}

fun IngredientRemote.mapToIngredientModel(): IngredientModel {
  return IngredientModel(
    id,
    categoryId,
    title,
    quantity,
    image
  )
}

fun List<CategoryRemote>.mapToCategoryModelList() =
  map { it.mapToCategoryModel() }

fun List<IngredientRemote>.mapToIngredientModelList() =
  map { it.mapToIngredientModel() }