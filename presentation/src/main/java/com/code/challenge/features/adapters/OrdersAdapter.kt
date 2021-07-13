package com.code.challenge.features.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.code.challenge.common.extensions.layoutInflater
import com.code.challenge.common.extensions.visible
import com.code.challenge.databinding.ItemOrderBinding
import com.code.challenge.domain.features.models.OrderModel
import timber.log.Timber

class OrdersAdapter(
  private val ordersCallback: OrdersCallback,
) : ListAdapter<OrderModel, OrdersAdapter.ViewHolder>(diffUtilItemCallback) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemOrderBinding.inflate(parent.layoutInflater, parent, false)
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let {
      holder.bindItem(it)
    }
  }

  inner class ViewHolder(private val binding: ItemOrderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindItem(item: OrderModel) {
      with(binding) {
        tvOrderTitle.text = item.title
        pbCounter.progress = item.progress.toInt()
        pbCounter.max = item.maxProgress.toInt()

        if (item.isExpired) {
          btnOrderAccept.visibility = View.INVISIBLE
          btnOrderExpired.visible(true)
        } else {
          Timber.tag("Worker_Counter").d("Progress: ${pbCounter.progress}")

          btnOrderAccept.visible(true)
          btnOrderExpired.visibility = View.INVISIBLE
        }

        btnOrderAccept.setOnClickListener {
          ordersCallback.onOrderAccepted(item)
        }
      }
    }
  }

  interface OrdersCallback {
    fun onOrderAccepted(selectedOrder: OrderModel)
  }

  companion object {
    val diffUtilItemCallback = object : DiffUtil.ItemCallback<OrderModel>() {
      override fun areItemsTheSame(
        oldItem: OrderModel,
        newItem: OrderModel,
      ): Boolean =
        oldItem.progress == newItem.progress

      override fun areContentsTheSame(
        oldItem: OrderModel,
        newItem: OrderModel,
      ): Boolean =
        oldItem == newItem
    }
  }
}