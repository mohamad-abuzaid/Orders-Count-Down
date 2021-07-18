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
    val response: String = when {
      url.encodedPath.endsWith("orders") -> readJsonFileFromAsset(0, context)
      url.encodedPath.endsWith("categories") -> readJsonFileFromAsset(1, context)
      url.toString().endsWith("ingredients?category_id=1") -> readJsonFileFromAsset(2, context)
      url.toString().endsWith("ingredients?category_id=2") -> readJsonFileFromAsset(3, context)
      url.toString().endsWith("ingredients?category_id=3") -> readJsonFileFromAsset(4, context)
      url.toString().endsWith("ingredients?category_id=4") -> readJsonFileFromAsset(5, context)
      url.toString().endsWith("ingredients?category_id=5") -> readJsonFileFromAsset(6, context)
      else -> readJsonFileFromAsset(-1, context)
    }

    return Response.Builder()
      .code(200)
      .message(response)
      .request(chain.request())
      .protocol(Protocol.HTTP_1_1)
      .body(response.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
      .addHeader("content-type", "application/json")
      .build()
  }

  private fun readJsonFileFromAsset(api: Int, context: Context): String {
    val json: String
    try {
      val inputStream: InputStream = when (api) {
        0 -> context.assets.open("OrdersList.json")
        1 -> context.assets.open("CategoriesList.json")
        2 -> context.assets.open("IngredientsList1.json")
        3 -> context.assets.open("IngredientsList2.json")
        4 -> context.assets.open("IngredientsList3.json")
        5 -> context.assets.open("IngredientsList4.json")
        6 -> context.assets.open("IngredientsList5.json")
        else -> context.assets.open("RequestError.json.json")
      }
      json = inputStream.bufferedReader().use { it.readText() }
    } catch (ex: Exception) {
      ex.printStackTrace()
      return ""
    }
    return json
  }
}