package com.code.challenge.di.workers

import androidx.work.WorkerFactory
import com.code.challenge.utils.workers.CountDownWorker
import com.code.challenge.utils.workers.CountDownWorkerFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WorkManagerModule {

  @Binds
  abstract fun bindsWorkManagerWorkerFactory(workManagerWorkerFactory: WorkManagerWorkerFactory): WorkerFactory

  @Binds @IntoMap @WorkerKey(CountDownWorker::class)
  abstract fun bindCountDownWorker(countDownWorkerFactory: CountDownWorkerFactory): CustomWorkerFactory
}