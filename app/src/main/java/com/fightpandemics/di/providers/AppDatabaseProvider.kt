package com.fightpandemics.di.providers

import android.content.Context
import androidx.room.Room
import com.fightpandemics.data.local.AppDatabase
import com.fightpandemics.data.local.DatabaseConstants

object AppDatabaseProvider {

    fun provide(context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DatabaseConstants.dbName
    ).build()
}