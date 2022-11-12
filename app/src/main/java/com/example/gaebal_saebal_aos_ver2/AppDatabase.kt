// Room 라이브러리를 활용한 앱 내 Database
package com.example.gaebal_saebal_aos_ver2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.*
import com.example.gaebal_saebal_aos_ver2.dao.CategoryDataDao
import com.example.gaebal_saebal_aos_ver2.dao.RecordDataDao
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity
import com.example.gaebal_saebal_aos_ver2.db_entity.RecordDataEntity
import java.io.ByteArrayOutputStream
import java.util.*

// 스키마 파일 수정하면 version 증가해야 함
@Database(entities = arrayOf(CategoryDataEntity::class, RecordDataEntity::class), version = 2, exportSchema = false)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun categoryDataDao(): CategoryDataDao
    abstract fun recordDataDao(): RecordDataDao

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
                    //.addMigrations(MIGRATION_1_2)
                    .build()
            }
            return instance
        }
        /*private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database : SupportSQLiteDatabase) {
                database.execSQL("DROP INDEX category_name")
            }
        }*/
    }
}

class RoomTypeConverter {

    // Bitmap -> ByteArray 변환
    @TypeConverter
    fun toByteArray(bitmap : Bitmap) : ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    // ByteArray -> Bitmap 변환
    @TypeConverter
    fun toBitmap(bytes : ByteArray) : Bitmap{
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) null else date.getTime()
    }
}