package com.code.challenge.utils.workers

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.code.challenge.di.workers.CustomWorkerFactory
import kotlinx.coroutines.delay
import javax.inject.Inject

@SuppressLint("HardwareIds")
class CountDownWorker @Inject constructor(
  appContext: Context,
  workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

  companion object {
    const val Order = "Order"
    const val Progress = "Progress"
    const val MaxProgress = "MaxProgress"
    const val Alerted = "Alerted"
    const val Completed = "Completed"
  }

  private val orderId: Long = inputData.getLong("order_id", 0L)
  private val alertTarget: Long = inputData.getLong("alert_target", 0L)
  private val expireTarget: Long = inputData.getLong("expired_target", 0L)
  private val step: Long = 60000L
  private var elapsed: Long = 0L

  override suspend fun doWork(): Result {
    while (alertTarget > elapsed) {
      delay(step)
      elapsed += step
      setProgress(workDataOf(Progress to elapsed,
        MaxProgress to expireTarget, Alerted to false, Completed to false, Order to orderId))
    }
    setProgress(workDataOf(Progress to elapsed, MaxProgress to expireTarget,
      Alerted to true, Completed to false, Order to orderId))

    while (expireTarget > elapsed) {
      delay(step)
      elapsed += step
      setProgress(workDataOf(Progress to elapsed, MaxProgress to expireTarget,
        Alerted to false, Completed to false, Order to orderId))
    }

    setProgress(workDataOf(Progress to elapsed, MaxProgress to expireTarget,
      Alerted to false, Completed to true, Order to orderId))
    return Result.success()
  }
}

class CountDownWorkerFactory @Inject constructor() : CustomWorkerFactory {

  override fun create(
    appContext: Context,
    workerParams: WorkerParameters,
  ): CoroutineWorker {
    return CountDownWorker(
      appContext,
      workerParams
    )
  }
}