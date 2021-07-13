package com.code.challenge.data.features.responses

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderRemote(

  @SerializedName("id")
  val id: Long,

  @SerializedName("title")
  val title: String,

  @SerializedName("addon")
  val addon: List<AddonRemote>?,

  @SerializedName("quantity")
  val quantity: Int,

  @SerializedName("created_at")
  val created_at: String,

  @SerializedName("alerted_at")
  val alerted_at: String,

  @SerializedName("expired_at")
  val expired_at: String,
)