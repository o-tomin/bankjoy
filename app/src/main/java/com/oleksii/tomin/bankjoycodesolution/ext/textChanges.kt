package com.oleksii.tomin.bankjoycodesolution.ext

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Click Listener for views using coroutine flows
 */
fun EditText.textChanges(): Flow<String> {
    return callbackFlow {
        addTextChangedListener {
            trySend(it.toStringOrEmpty())
        }
        awaitClose { addTextChangedListener(null) }
    }
}