package com.fightpandemics.data.local

import androidx.room.RoomDatabase
import com.fightpandemics.data.local.dao.Dao

/**
 * This will be the App Database Class
 * Please refactor to make use of custom names
 */
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}