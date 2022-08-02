package com.hyunju.deliveryapp.util.provider

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class DefaultResourcesProvider(
    private val context: Context
) : ResourcesProvider {

    override fun getString(@StringRes resId: Int): String = context.getString(resId)

    override fun getString(@StringRes resId: Int, vararg formArg: Any): String =
        context.getString(resId, formArg)

    override fun getColor(@ColorRes resId: Int): Int = ContextCompat.getColor(context, resId)

    override fun getColorStateList(@ColorRes resId: Int): ColorStateList =
        context.getColorStateList(resId)
}