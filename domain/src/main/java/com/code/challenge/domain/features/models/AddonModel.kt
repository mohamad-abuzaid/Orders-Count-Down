package com.code.challenge.domain.features.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AddonModel(

  val id: Long,

  val title: String,

  val quantity: Int,

  ) : Parcelable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is OrderModel) return false
    if (id != other.id) return false
    if (title != other.title) return false
    if (quantity != other.quantity) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + title.hashCode()
    result = 31 * result + quantity.hashCode()

    return result
  }
}