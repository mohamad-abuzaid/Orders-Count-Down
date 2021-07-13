package com.code.challenge.data.utils.parser

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
class CodeResponse<T>(
  val success: Boolean,
  val data: T,
) : Serializable