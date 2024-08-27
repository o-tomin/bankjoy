package com.oleksii.tomin.bankjoycodesolution.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory

object LoginViewModelFactory : Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}