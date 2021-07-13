package com.code.challenge.features.fragments

import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.code.challenge.R
import com.code.challenge.common.base.views.BaseFragmentViewBinding
import com.code.challenge.common.di.ViewModelFactory
import com.code.challenge.common.extensions.makeSnackBar
import com.code.challenge.common.utils.Q
import com.code.challenge.databinding.FragmentOrdersBinding
import com.code.challenge.domain.common.Resource
import com.code.challenge.domain.common.ResourceType.LOADING
import com.code.challenge.domain.common.ResourceType.SUCCESS
import com.code.challenge.domain.features.models.OrderModel
import com.code.challenge.features.adapters.OrdersAdapter
import com.code.challenge.features.adapters.OrdersAdapter.OrdersCallback
import com.code.challenge.features.viewmodels.OrdersFragmentViewModel
import com.code.challenge.utils.extensions.appComponent
import com.code.challenge.utils.extensions.showProgressWheel
import com.code.challenge.utils.workers.CountDownWorker
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class OrdersFragment : BaseFragmentViewBinding<FragmentOrdersBinding>(), OrdersCallback {

  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  private val ordersFragmentViewModel: OrdersFragmentViewModel by activityViewModels { viewModelFactory }

  private val ordersAdapter: OrdersAdapter by lazy { OrdersAdapter(this) }

  private var dateFormat: SimpleDateFormat? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm+ss'Z'")

  override fun onBind(
    inflater: LayoutInflater,
    container: ViewGroup?,
  ): FragmentOrdersBinding {
    appComponent.inject(this)
    return FragmentOrdersBinding.inflate(inflater, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    super.onViewCreated(view, savedInstanceState)

    setupOrdersRecycler()
    initClickListeners()
    initObservers()

    fetchInitialData()
  }

  private fun setupOrdersRecycler() {
    val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    binding.rvOrders.layoutManager = layoutManager
    binding.rvOrders.adapter = ordersAdapter
  }

  private fun initClickListeners() {
    binding.btnIngredients.setOnClickListener {
      findNavController().navigate(R.id.ingredientsFragment)
    }
  }

  private fun fetchInitialData() {
    ordersFragmentViewModel.fetchOrdersList()
  }

  private fun initObservers() {
    ordersFragmentViewModel.ordersListResult.observe(viewLifecycleOwner, this::ordersListObserver)
  }

  private fun ordersListObserver(result: Resource<List<OrderModel>>) {
    when (result.resourceType) {
      LOADING -> activity?.showProgressWheel(true)
      SUCCESS -> {
        activity?.showProgressWheel(false)
        result.data?.let { orders ->
          orders.forEach {
            if (ordersFragmentViewModel.openOrderWorkers.contains(it.id).not()) {
              ordersFragmentViewModel.openOrderWorkers.add(it.id)
              startTimers(it.id, it.alerted_at, it.expired_at)
            }
          }
          ordersAdapter.submitList(orders)
        }
      }
      else -> {
        activity?.showProgressWheel(false)
        binding.ordersContainer.makeSnackBar(
          Q.SNACK.FAIL,
          getString(R.string.server_error_message),
          getString(R.string.dismiss)
        ) {}
      }
    }
  }

  private fun startTimers(orderId: Long, alertedAt: String, expiredAt: String) {
    val alertTarget = (dateFormat?.parse(alertedAt)?.time ?: 0L) - Date().time
    val expireTarget = (dateFormat?.parse(expiredAt)?.time ?: 0L) - Date().time

    if (expireTarget > 0) {
      onOrderProgressUpdated(orderId, 0L, expireTarget)

      val inputData = Data.Builder()
        .putLong("order_id", orderId)
        .putLong("alert_target", alertTarget)
        .putLong("expired_target", expireTarget)
        .build()

      val countDownWorker = OneTimeWorkRequestBuilder<CountDownWorker>().setInputData(inputData).build()
      WorkManager.getInstance(requireContext()).enqueue(countDownWorker)

      WorkManager.getInstance(requireContext())
        .getWorkInfoByIdLiveData(countDownWorker.id)
        .observe(viewLifecycleOwner, { workInfo: WorkInfo? ->
          if (workInfo != null) {
            val progress = workInfo.progress
            val workerProgress = progress.getLong(CountDownWorker.Progress, 0L)
            val workerMaxProgress = progress.getLong(CountDownWorker.MaxProgress, 0L)
            val workerOrderId = progress.getLong(CountDownWorker.Order, 0L)
            val alerted = progress.getBoolean(CountDownWorker.Alerted, false)
            val completed = progress.getBoolean(CountDownWorker.Completed, false)

            onOrderProgressUpdated(workerOrderId, workerProgress, workerMaxProgress)

//            if (alerted) playRingtone()
            if (completed) onOrderExpired(workerOrderId)
          }
        })

    } else {
      onOrderExpired(orderId)
    }
  }

  private fun onOrderProgressUpdated(orderId: Long, progress: Long, maxProgress: Long) {
    ordersFragmentViewModel.ordersList.map {
      if (it.id == orderId) {
        it.progress = progress
        if (maxProgress > 0L) it.maxProgress = maxProgress
      }
    }
    ordersAdapter.notifyDataSetChanged()
  }

  override fun onOrderAccepted(selectedOrder: OrderModel) {
    ordersFragmentViewModel.ordersList.remove(selectedOrder)
    ordersAdapter.notifyDataSetChanged()
  }

  private fun playRingtone() {
    val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    val r = RingtoneManager.getRingtone(requireContext(), notification)
    r.play()
  }

  private fun onOrderExpired(orderId: Long) {
    ordersFragmentViewModel.openOrderWorkers.remove(orderId)

    ordersFragmentViewModel.ordersList.map {
      if (it.id == orderId) {
        it.isExpired = true
      }
    }
    ordersAdapter.notifyDataSetChanged()
  }

  override fun onDetach() {
    activity?.showProgressWheel(false)
    super.onDetach()
  }
}