package com.example.mycryptoapp.domain

class GetCoinInfoListUseCase(
    private val coinRepository: CoinRepository
) {
    operator fun invoke() = coinRepository.getCoinInfoList()
}