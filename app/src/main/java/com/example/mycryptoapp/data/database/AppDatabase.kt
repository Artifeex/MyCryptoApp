package com.example.mycryptoapp.data.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CoinInfoDbModel::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun coinInfoDao(): CoinInfoDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        private val LOCK = Any()

        private const val DB_NAME = "coin_items.db"

        fun getInstance(context: Context): AppDatabase {
            INSTANCE?.let { return it }
            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val db =
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DB_NAME
                    )
                        //после изменения типа у поля внутри CoinInfoDbModel нужно обновить БД. ДЛя этого нужно писать миграции
                        //но если мы не хотим сохранить предыдущие данные, то ииспользуем эту функцию. Данные просто удалятся после миграции
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = db
                return db
            }
        }
    }

}