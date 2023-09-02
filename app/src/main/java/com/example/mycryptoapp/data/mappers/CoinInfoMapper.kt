package com.example.mycryptoapp.data.mappers

import com.example.mycryptoapp.data.database.CoinInfoDbModel
import com.example.mycryptoapp.data.network.model.CoinInfoDto
import com.example.mycryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.mycryptoapp.data.network.model.CoinNamesListDto
import com.example.mycryptoapp.domain.CoinInfo
import com.google.gson.Gson
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CoinInfoMapper {

    fun mapDtoToDbModel(coinInfoDto: CoinInfoDto): CoinInfoDbModel = CoinInfoDbModel(
        fromSymbol = coinInfoDto.fromSymbol,
        toSymbol = coinInfoDto.toSymbol,
        price = coinInfoDto.price,
        lowDay = coinInfoDto.lowDay,
        highDay = coinInfoDto.highDay,
        lastUpdate = coinInfoDto.lastUpdate,
        lastMarket = coinInfoDto.lastMarket,
        imageUrl = BASE_IMAGE_URL + coinInfoDto.imageUrl
    )

    fun mapJsonContainerToListCoinInfo(jsonContainer: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = jsonContainer.coinPriceInfoJsonObject ?: return result
        val coinKeySet = jsonObject.keySet() //BTC, ETH - fSyms, которые получили другим запросом
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey) //  {USD: {JSON} }
            val currencyKeySet = currencyJson.keySet() // USD
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey), //{USD: {JSON} } - из этого объекта по ключу USD получаем внутренний {JSON}, в котором хранится вся инфа
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    fun mapNamesListToString(namesListDto: CoinNamesListDto): String {
        return namesListDto.names?.map { it.coinName?.name }?.joinToString(",") ?: ""
    }

    private fun convertTimestampToTime(timestamp: Long?): String {
        if (timestamp == null) return ""
        val stamp = Timestamp(timestamp * 1000)
        val date = Date(stamp.time)
        val pattern = "HH:mm:ss"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    fun mapDbModelToEntity(coinInfoDbModel: CoinInfoDbModel): CoinInfo {
        return CoinInfo(
            fromSymbol = coinInfoDbModel.fromSymbol,
            toSymbol = coinInfoDbModel.toSymbol,
            price = coinInfoDbModel.price,
            minPriceDay = coinInfoDbModel.lowDay,
            maxPriceDay = coinInfoDbModel.highDay,
            lastUpdate = convertTimestampToTime(coinInfoDbModel.lastUpdate),
            lastMarket = coinInfoDbModel.lastMarket,
            imageUrl = coinInfoDbModel.imageUrl
        )
    }

    companion object {
        const val BASE_IMAGE_URL = "https://cryptocompare.com"
    }

}