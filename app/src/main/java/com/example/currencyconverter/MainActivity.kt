package com.example.currencyconverter

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.let {
            it.btnConvert.setOnClickListener { _ ->
                viewModel.convertCurrency(
                    it.etFrom.text.toString(),
                    it.spFromCurrency.selectedItem.toString(),
                    it.spToCurrency.selectedItem.toString()
                )
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {events ->
                when(events) {
                    is MainViewModel.CurrencyEvents.Success -> {
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.BLACK)
                        binding.tvResult.text = events.resultText
                    }
                    is MainViewModel.CurrencyEvents.Failure -> {
                        binding.progressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.RED)
                        binding.tvResult.text = events.errorText
                    }
                    is MainViewModel.CurrencyEvents.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }
}