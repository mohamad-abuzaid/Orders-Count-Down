package com.code.challenge.domain.features.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class OrderModel(

  val id: Long,

  val title: String,

  val addon: List<AddonModel>?,

  val quantity: Int,

  val created_at: String,

  val alerted_at: String,

  val expired_at: String,

  var progress: Long = 0,

  var maxProgress: Long = 0,

  var isExpired: Boolean = false,

  var isAccepted: Boolean = false,

  ) : Parcelable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is OrderModel) return false
    if (id != other.id) return false
    if (title != other.title) return false
    if (quantity != other.quantity) return false
    if (created_at != other.created_at) return false
    if (alerted_at != other.alerted_at) return false
    if (expired_at != other.expired_at) return false
    if (progress != other.progress) return false
    if (maxProgress != other.maxProgress) return false
    if (isAccepted != other.isAccepted) return false
    if (isExpired != other.isExpired) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + title.hashCode()
    result = 31 * result + quantity.hashCode()
    result = 31 * result + created_at.hashCode()
    result = 31 * result + alerted_at.hashCode()
    result = 31 * result + expired_at.hashCode()
    result = 31 * result + progress.hashCode()
    result = 31 * result + maxProgress.hashCode()
    result = 31 * result + isAccepted.hashCode()
    result = 31 * result + isExpired.hashCode()

    return result
  }
}