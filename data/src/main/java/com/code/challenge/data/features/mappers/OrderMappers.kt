package com.code.challenge.data.features.mappers

import com.code.challenge.data.features.responses.AddonRemote
import com.code.challenge.data.features.responses.OrderRemote
import com.code.challenge.domain.features.models.AddonModel
import com.code.challenge.domain.features.models.OrderModel

fun OrderRemote.mapToOrderModel(): OrderModel {
  return OrderModel(
    id,
    title,
    addon?.mapToAddonModelList(),
    quantity,
    created_at,
    alerted_at,
    expired_at
  )
}

fun AddonRemote.mapToAddonModel(): AddonModel {
  return AddonModel(
    id,
    title,
    quantity,
  )
}

fun List<OrderRemote>.mapToOrderModelList() =
  map { it.mapToOrderModel() }

fun List<AddonRemote>.mapToAddonModelList() =
  map { it.mapToAddonModel() }