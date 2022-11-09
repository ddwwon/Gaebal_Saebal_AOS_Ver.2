// Room 라이브러리를 활용한 앱 내 Database
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gaebal_saebal_aos_ver2.dao.CategoryDataDao
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity

@Database(entities = [CategoryDataEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun categoryDataDao(): CategoryDataDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-category"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}