package com.oleksii.tomin.bankjoycodesolution.ext

import android.util.Log

fun eLog(e: Throwable) =
    Log.e("BankJoy", e.message, e)