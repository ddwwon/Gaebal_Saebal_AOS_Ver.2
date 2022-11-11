// 카테고리 DB DAO(쿼리) 정의
package com.example.gaebal_saebal_aos_ver2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gaebal_saebal_aos_ver2.db_entity.CategoryDataEntity

@Dao
interface CategoryDataDao {
    @Query("SELECT * FROM CategoryDataEntity") // 전체 데이터
    fun getAllCategoryData(): MutableList<CategoryDataEntity>

    @Insert
    fun insertCategoryData(category: CategoryDataEntity) // 데이터 추가

    @Delete
    fun deleteCategoryData(category: CategoryDataEntity) // 데이터 삭제

    @Query("DELETE FROM CategoryDataEntity") // 데이터 전체 삭제
    fun deleteAllCategoryData()

    @Query("SELECT COUNT(*) FROM CategoryDataEntity") // 데이터 갯수
    fun getSizeCategoryData(): Int

    @Query("SELECT COUNT(*) FROM CategoryDataEntity WHERE category_name LIKE :categoryName") // 데이터 존재유무
    fun checkExistCategoryData(categoryName: String): Int

    @Query("SELECT category_name FROM CategoryDataEntity WHERE category_uid == :key") // id 값으로 카테고리명 가져오기
    fun getCategoryName(key: Int): String
}