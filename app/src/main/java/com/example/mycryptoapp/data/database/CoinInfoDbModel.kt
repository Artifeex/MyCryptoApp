package com.example.mycryptoapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "full_price_list")
data class CoinInfoDbModel(
    @PrimaryKey
    val fromSymbol: String,
    val toSymbol: String?,
    val price: String?,
    val lowDay: String?,
    val highDay: String?,
    val lastUpdate: Long?, //long, потому что сортируем внутри БД по последнему обновлению
    val lastMarket: String?,
    val imageUrl: String
)
