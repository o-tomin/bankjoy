package com.oleksii.tomin.bankjoycodesolution.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oleksii.tomin.bankjoycodesolution.R
import com.oleksii.tomin.bankjoycodesolution.databinding.FragmentLoginFormBinding
import com.oleksii.tomin.bankjoycodesolution.ext.collectWithLifecycle
import com.oleksii.tomin.bankjoycodesolution.ext.scopedClickAndDebounce
import com.oleksii.tomin.bankjoycodesolution.ext.scopedTextChangesAndDebounce
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class LoginBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentLoginFormBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginFormBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider.create(
            owner = this,
            factory = LoginViewModelFactory
        )[LoginViewModel::class]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state
            .drop(1)
            .map { it.username to it.password }
            .collectWithLifecycle(this) { (username, password) ->
                if (username.isValid && password.isValid) {
                    setPasswordFieldToValidState()
                } else {
                    setPasswordFieldToErrorState()
                }
            }

        viewModel.state
            .map { it.isContinueEnabled }
            .collectWithLifecycle(this) {
                binding.continueButton.isEnabled = it
            }

        viewModel.state
            .map { it.isRememberMeChecked }
            .collectWithLifecycle(this) {
                binding.rememberMe.isChecked = it
            }

        viewModel.state
            .map { it.isRememberEnabled }
            .collectWithLifecycle(this) {
                binding.rememberMe.isEnabled = it
            }

        viewModel.events
            .collectWithLifecycle(this) {
                when (it) {
                    LoginViewModel.LoginViewModelEvents.LoginFailed -> {
                        binding.rememberMe.isChecked = false
                        binding.rememberMe.isEnabled = false
                        setPasswordFieldToErrorState()
                    }
                }
            }

        with(binding) {
            usernameField.scopedTextChangesAndDebounce().buffer().onEach {
                viewModel.validateUsername(it)
            }.launchIn(lifecycleScope)

            passwordField.scopedTextChangesAndDebounce().buffer().onEach {
                viewModel.validatePassword(it)
            }.launchIn(lifecycleScope)

            continueButton.scopedClickAndDebounce().onEach {
                viewModel.logIn()
            }.launchIn(lifecycleScope)
        }
    }

    private fun setPasswordFieldToErrorState() = with(binding) {
        password.error = getString(R.string.login_error_message)
        passwordField.apply {
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.error_color
                )
            )
            ViewCompat.setBackgroundTintList(
                this,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.error_color
                    )
                )
            )
        }
    }

    private fun setPasswordFieldToValidState() = with(binding) {
        password.error = null
        passwordField.apply {
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )

            ViewCompat.setBackgroundTintList(
                this,
                ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.black)
                )
            )
        }
    }

    companion object {
        const val TAG = "LoginBottomSheet"
    }
}