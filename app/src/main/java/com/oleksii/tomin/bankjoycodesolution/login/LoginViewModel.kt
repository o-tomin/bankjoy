package com.oleksii.tomin.bankjoycodesolution.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        LoginViewModelState(
            username = Username("", false),
            password = Password("", false),
            isRememberMeChecked = false,
            isContinueEnabled = false,
        )
    )
    private val _events = MutableSharedFlow<LoginViewModelEvents>(1, 0, BufferOverflow.DROP_LATEST)
    val state: StateFlow<LoginViewModelState>
        get() = _state
    val events: Flow<LoginViewModelEvents>
        get() = _events

    private val stateMutex = Mutex()

    private suspend fun setState(reducer: LoginViewModelState.() -> LoginViewModelState) {
        _state.value = stateMutex.withLock {
            _state.value.reducer()
        }
    }

    fun validateUsername(userInput: String) = viewModelScope.launch(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    fun validatePassword(userInput: String) = viewModelScope.launch(Dispatchers.IO) {
        TODO("Not yet implemented")
    }

    data class LoginViewModelState(
        val username: Username,
        val password: Password,
        val isRememberMeChecked: Boolean,
        val isContinueEnabled: Boolean,
    )

    sealed class LoginViewModelEvents

    data class Username(val username: String, val isValid: Boolean)
    data class Password(val password: String, val isValid: Boolean)
}
