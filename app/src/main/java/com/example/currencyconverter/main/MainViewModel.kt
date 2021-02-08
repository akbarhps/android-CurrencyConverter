package com.example.currencyconverter.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.currencyconverter.util.DispatcherProvider

class MainViewModel @ViewModelInject constructor(
    private val repositoryServices: MainRepositoryServices,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
}