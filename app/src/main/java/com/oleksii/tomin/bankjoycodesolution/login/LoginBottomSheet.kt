package com.oleksii.tomin.bankjoycodesolution.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oleksii.tomin.bankjoycodesolution.databinding.FragmentLoginFormBinding
import com.oleksii.tomin.bankjoycodesolution.ext.collectWithLifecycle
import com.oleksii.tomin.bankjoycodesolution.ext.scopedTextChangesAndDebounce
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
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
            .map { it.username.isValid && it.password.isValid }
            .distinctUntilChanged()
            .collectWithLifecycle(this) { isUserInputValid ->
                updateUI(isUserInputValid)
            }

        with(binding) {
            usernameField.scopedTextChangesAndDebounce().buffer().onEach {
                viewModel.validateUsername(it)
            }.launchIn(lifecycleScope)
            passwordField.scopedTextChangesAndDebounce().buffer().onEach {
                viewModel.validatePassword(it)
            }.launchIn(lifecycleScope)
        }
    }

    suspend fun updateUI(isUserInputValid: Boolean) {
        TODO("Not yet implemented")
    }

    companion object {
        const val TAG = "LoginBottomSheet"
    }
}