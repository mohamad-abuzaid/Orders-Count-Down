package com.code.challenge.domain.features.models

import androidx.annotation.Keep

@Keep
data class IngredientModel(

  val id: Long,

  val categoryId: Long,

  val title: String,

  val quantity: Int,

  val image: String,

  )