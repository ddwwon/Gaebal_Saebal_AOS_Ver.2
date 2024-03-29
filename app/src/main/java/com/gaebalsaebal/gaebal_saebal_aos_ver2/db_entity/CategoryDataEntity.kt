// 카테고리 DB Entity(테이블) 정의
package com.gaebalsaebal.gaebal_saebal_aos_ver2.db_entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// (indices = [Index(value = ["category_name"], unique = true)])
@Entity
data class CategoryDataEntity (
    @PrimaryKey(autoGenerate = true)
    val category_uid: Int,    // id = key

    @ColumnInfo
    val category_name: String // 카테고리명
)