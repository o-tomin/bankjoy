package com.oleksii.tomin.bankjoycodesolution

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.oleksii.tomin.bankjoycodesolution.databinding.ActivityMainBinding
import com.oleksii.tomin.bankjoycodesolution.ext.scopedClickAndDebounce
import com.oleksii.tomin.bankjoycodesolution.ext.viewBinding
import com.oleksii.tomin.bankjoycodesolution.login.LoginBottomSheet
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpView()
    }

    private fun setUpView() = with(binding) {

        showLogin()

        mainActivityLayout.scopedClickAndDebounce().onEach {
            showLogin()
        }.launchIn(lifecycleScope)
    }

    private fun showLogin() = LoginBottomSheet().show(supportFragmentManager, LoginBottomSheet.TAG)
}

