package com.code.challenge.data.features.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryRemote(

  @SerializedName("id")
  val id: Long,

  @SerializedName("title")
  val title: String,

  )