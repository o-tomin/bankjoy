package com.oleksii.tomin.bankjoycodesolution.ext

import android.text.Editable

fun Editable?.toStringOrEmpty() = this?.toString() ?: ""
