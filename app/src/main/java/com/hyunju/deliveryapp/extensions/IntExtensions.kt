package com.hyunju.deliveryapp.extensions

import java.text.DecimalFormat

fun Int.fromNumberToPrice(): String {
    return DecimalFormat("#,###").format(this)
}
