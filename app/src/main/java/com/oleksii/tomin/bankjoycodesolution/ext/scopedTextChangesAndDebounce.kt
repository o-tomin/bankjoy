package com.oleksii.tomin.bankjoycodesolution.ext

import android.widget.EditText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
fun EditText.scopedTextChangesAndDebounce(debounceTime: Long = 300L) =
    textChanges().debounce(debounceTime)
