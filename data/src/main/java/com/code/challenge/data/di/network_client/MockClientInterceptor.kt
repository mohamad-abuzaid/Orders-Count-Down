package com.code.challenge.data.di.network_client

import android.content.Context
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class MockClientInterceptor @Inject constructor(private val context: Context) : Interceptor {
  @Throws(IOException::class)
  override fun intercept(chain: Chain): Response {
    val url: HttpUrl = chain.request().url
    return when {
      url.encodedPath.endsWith("list") -> {
        val response: String = readJsonFileFromAsset(context)
        Response.Builder()
          .code(200)
          .message(response)
          .request(chain.request())
          .protocol(Protocol.HTTP_1_1)
          .body(response.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
          .addHeader("content-type", "application/json")
          .build()
      }
      else -> chain.proceed(chain.request())
    }
  }

  private fun readJsonFileFromAsset(context: Context): String {
    val json: String
    try {
      val inputStream: InputStream = context.assets.open("OrdersList.json")
      json = inputStream.bufferedReader().use { it.readText() }
    } catch (ex: Exception) {
      ex.printStackTrace()
      return ""
    }
    return json
  }
}