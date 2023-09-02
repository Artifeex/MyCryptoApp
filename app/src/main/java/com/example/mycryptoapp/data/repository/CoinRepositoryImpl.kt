package com.example.mycryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.mycryptoapp.data.database.AppDatabase
import com.example.mycryptoapp.data.mappers.CoinInfoMapper
import com.example.mycryptoapp.data.network.ApiFactory
import com.example.mycryptoapp.data.workers.RefreshDataWorker
import com.example.mycryptoapp.domain.CoinInfo
import com.example.mycryptoapp.domain.CoinRepository
import kotlinx.coroutines.delay

class CoinRepositoryImpl(private val application: Application): CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinInfoDao()
    private val apiService = ApiFactory.apiService

    private val mapper = CoinInfoMapper()

    override fun getCoinInfoList(): LiveData<List<CoinInfo>> {
        //Так как наша база данных возвращает объект CoinInfoDbModel, а мы из репозитория должны вернуть CoinInfo, то мы используем перехват liveData и преобразовы
        //ваем ее в то, что нам нужно и возвращается отсюда тоже LiveData.
       return coinInfoDao.getPriceList().map {
           it.map {
               mapper.mapDbModelToEntity(it)
           }
       }
    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> {
        return coinInfoDao.getPriceInfoAboutCoin(fromSymbol).map {
            mapper.mapDbModelToEntity(it)
        }
    }

    //до внедрения воркера она была suspend и загрузку данных мы делали внутри нее. А во ViewModel запускали UseCase внутри viewModelScope. launch {}

    override suspend fun loadData() {
        while (true) {
            try {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val fSyms = mapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getCoinInfo(fSyms = fSyms)
                val coinInfoDtoList = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
                val dbModelList = coinInfoDtoList.map { mapper.mapDtoToDbModel(it) }
                coinInfoDao.insertPriceList(dbModelList)
            } catch (e: Exception) {
            }
            delay(10000)
        }
//        val workManager = WorkManager.getInstance(application)
//        workManager.enqueueUniqueWork(
//            RefreshDataWorker.NAME,
//            ExistingWorkPolicy.REPLACE,
//            RefreshDataWorker.makeRequest()
//        )
    }
}