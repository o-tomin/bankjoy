package com.oleksii.tomin.bankjoycodesolution.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oleksii.tomin.bankjoycodesolution.ext.eLog
import com.oleksii.tomin.bankjoycodesolution.ext.iLog
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
            isRememberEnabled = false,
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
        val isUserInputValid = userInput.isUsernameValid()
        val isPasswordValid = userInput.isPasswordValid()
        val isCredentialsValid = isPasswordValid && state.value.username.isValid
        setState {
            copy(
                username = Username(
                    username = userInput,
                    isValid = isUserInputValid,
                ),
                isContinueEnabled = isUserInputValid && password.isValid,
                isRememberEnabled = isCredentialsValid,
                isRememberMeChecked = if (!isCredentialsValid) false else isRememberMeChecked
            )
        }
    }

    /**
     * Username validated to match requirements:
     * 1. Contain at least 5 characters;
     * 2. Do not start from special characters;
     * 3. Contain at least 3 digits;
     * 4. Contain at least one special character.
     *
     */
    private fun String.isUsernameValid(): Boolean {
        if (this.length < 5) {
            iLog("Username should contain at least 5 characters")
            return false
        }
        if (!this.first().isCharValidLetter()) {
            iLog("Username should not start from number or special character")
            return false
        }
        if (!containsAtLeastThreeDigits()) {
            iLog("Username should contain at least 3 digits")
            return false
        }
        if (!containsAtLeastOneSpecialCharacter()) {
            iLog("Username should contain at least 1 special character")
            return false
        }

        return true
    }

    private fun Char.isCharValidLetter() =
        "^[a-zA-Z]$".toRegex().matches(this.toString())

    private fun String.containsAtLeastThreeDigits() =
        "(\\D*\\d\\D*){3,}".toRegex().containsMatchIn(this)

    private fun String.containsAtLeastOneSpecialCharacter() =
        "[^a-zA-Z0-9]".toRegex().containsMatchIn(this)

    fun validatePassword(userInput: String) = viewModelScope.launch(Dispatchers.IO) {
        val isPasswordValid = userInput.isPasswordValid()
        val isCredentialsValid = isPasswordValid && state.value.username.isValid
        setState {
            copy(
                password = Password(
                    password = userInput,
                    isValid = isPasswordValid,
                ),
                isContinueEnabled = isCredentialsValid,
                isRememberEnabled = isCredentialsValid,
                isRememberMeChecked = if (!isCredentialsValid) false else isRememberMeChecked
            )
        }
    }

    /**
     * Password validated to match requirements
     * 1. Password should be at least 8 characters long
     * 2. Password cant contain username
     *
     */
    private fun String.isPasswordValid(): Boolean {
        if (this.length < 8) {
            iLog("Password should be at least 8 characters long")
            return false
        }

        if (this.contains(state.value.username.username)) {
            iLog("Password should not contain username")
            return false
        }
        return true
    }

    fun logIn() = viewModelScope.launch(Dispatchers.IO) {
        try {
            throw Exception()
        } catch (e: Throwable) {
            eLog(e)
            _events.emit(LoginViewModelEvents.LoginFailed)
        }
    }

    data class LoginViewModelState(
        val username: Username,
        val password: Password,
        val isRememberMeChecked: Boolean,
        val isRememberEnabled: Boolean,
        val isContinueEnabled: Boolean,
    )

    sealed class LoginViewModelEvents {
        data object LoginFailed : LoginViewModelEvents()
    }

    data class Username(val username: String, val isValid: Boolean)
    data class Password(val password: String, val isValid: Boolean)
}
