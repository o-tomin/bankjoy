package com.oleksii.tomin.bankjoycodesolution.ext

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline factory: (LayoutInflater) -> T
) =
    lazy(LazyThreadSafetyMode.NONE) {
        factory(layoutInflater)
    }