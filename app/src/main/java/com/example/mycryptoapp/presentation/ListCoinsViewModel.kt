package com.example.mycryptoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mycryptoapp.data.repository.CoinRepositoryImpl
import com.example.mycryptoapp.domain.CoinInfo
import com.example.mycryptoapp.domain.GetCoinInfoListUseCase
import com.example.mycryptoapp.domain.GetCoinInfoUseCase
import com.example.mycryptoapp.domain.LoadDataUseCase
import kotlinx.coroutines.launch

class ListCoinsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CoinRepositoryImpl(application)

    private val getCoinInfoListUseCase = GetCoinInfoListUseCase(repository)
    private val getCoinInfoUseCase = GetCoinInfoUseCase(repository)
    private val loadDataUseCase = LoadDataUseCase(repository)

    val coinInfoList = getCoinInfoListUseCase()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            loadDataUseCase()
        }
    }

    fun getDetailCoinInfo(fromSymbol: String) = getCoinInfoUseCase(fromSymbol)


}