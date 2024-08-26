package com.oleksii.tomin.bankjoycodesolution

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.oleksii.tomin.bankjoycodesolution.databinding.ActivityMainBinding
import com.oleksii.tomin.bankjoycodesolution.ext.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}

