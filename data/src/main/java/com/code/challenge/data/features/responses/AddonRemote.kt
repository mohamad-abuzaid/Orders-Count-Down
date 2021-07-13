package com.code.challenge.data.features.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.util.Date

@Keep
data class AddonRemote(

  @SerializedName("id")
  val id: Long,

  @SerializedName("title")
  val title: String,

  @SerializedName("quantity")
  val quantity: Int,
)