package com.hyunju.deliveryapp.util.provider

import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface ResourcesProvider {

    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg formArg: Any): String

    fun getColor(@ColorRes resId: Int): Int

    fun getColorStateList(@ColorRes resId: Int): ColorStateList
}