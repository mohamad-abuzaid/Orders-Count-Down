package com.code.challenge.data.di.network_client

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
class InterceptorsModule {

  @Provides
  @Singleton
  fun providesRequestErrorInterceptor(): Interceptor = RequestErrorInterceptor()

  @Provides
  @Singleton
  fun providesMockClientInterceptor(appContext: Context): Interceptor = MockClientInterceptor(appContext)
}