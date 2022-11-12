// 기록 내용 DB DAO(쿼리) 정의
package com.example.gaebal_saebal_aos_ver2.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.gaebal_saebal_aos_ver2.db_entity.RecordDataEntity

@Dao
interface RecordDataDao {
    @Query("SELECT * FROM RecordDataEntity ORDER BY record_date") // 전체 데이터
    fun getAllRecordData(): MutableList<RecordDataEntity>

    @Insert
    fun insertRecordData(category: RecordDataEntity) // 데이터 추가

    @Delete
    fun deleteRecordData(category: RecordDataEntity) // 데이터 삭제

    @Query("DELETE FROM RecordDataEntity WHERE record_uid = :recordUid") // id 이용해서 데이터 삭제
    fun deleteRecordFromUid(recordUid: Int)

    @Query("DELETE FROM RecordDataEntity") // 데이터 전체 삭제
    fun deleteAllRecordData()

    @Query("SELECT * FROM RecordDataEntity WHERE record_uid = :recordUid") // id 이용해서 record 불러오기
    fun getRecordContent(recordUid: Int): RecordDataEntity

    @Query("SELECT * FROM RecordDataEntity WHERE record_category_uid = :categoryUid ORDER BY record_date") // 해당 카테고리의 record 불러오기
    fun getRecordFromCategory(categoryUid: Int): MutableList<RecordDataEntity>

    // 검색
    @Query("SELECT * FROM RecordDataEntity WHERE record_contents LIKE '%'||:searchWord||'%' OR record_tags LIKE '%'||:searchWord||'%'")
    fun searchResult(searchWord: String): MutableList<RecordDataEntity>
}
// 데이터 수정 쿼리도 추가