package com.example.mycryptoapp.domain

data class CoinInfo(
    val fromSymbol: String,
    val toSymbol: String?,
    val price: String?,
    val minPriceDay: String?,
    val maxPriceDay: String?,
    val lastUpdate: String,
    val lastMarket: String?,
    val imageUrl: String
)
