package com.code.challenge.di.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

interface CustomWorkerFactory {
  fun create(
    appContext: Context,
    workerParams: WorkerParameters
  ): CoroutineWorker
}

class WorkManagerWorkerFactory @Inject constructor(
  private val workerFactories: Map<Class<out CoroutineWorker>, @JvmSuppressWildcards Provider<CustomWorkerFactory>>
) : WorkerFactory() {
  override fun createWorker(
    appContext: Context,
    workerClassName: String,
    workerParameters: WorkerParameters
  ): CoroutineWorker {
    val foundEntry =
      workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
    val factoryProvider = foundEntry?.value
      ?: throw IllegalArgumentException("unknown worker class name: $workerClassName")
    return factoryProvider.get().create(appContext, workerParameters)
  }
}