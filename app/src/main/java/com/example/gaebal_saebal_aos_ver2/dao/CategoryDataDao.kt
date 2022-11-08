// 카테고리 DB DAO(쿼리) 정의
package com.example.gaebal_saebal_aos_ver2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity

@Dao
interface CategoryDataDao {
    @Query("SELECT * FROM CategoryDataEntity")
    fun getAllCategoryData(): List<CategoryDataEntity>

    @Insert
    fun insertCategoryData(category: CategoryDataEntity)

    @Delete
    fun deleteCategoryData(category: CategoryDataEntity)

    @Query("DELETE FROM CategoryDataEntity")
    fun deleteAllCategoryData()
}