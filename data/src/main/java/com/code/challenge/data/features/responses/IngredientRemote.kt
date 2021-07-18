package com.code.challenge.data.features.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class IngredientRemote(

  @SerializedName("id")
  val id: Long,

  @SerializedName("category_id")
  val categoryId: Long,

  @SerializedName("title")
  val title: String,

  @SerializedName("quantity")
  val quantity: Int,

  @SerializedName("image")
  val image: String,

  )