// 카테고리 DB Entity(테이블) 정의
package com.example.gaebal_saebal_aos_ver2.db_entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryDataEntity (
    @PrimaryKey val category_uid: Int,    // id = key
    @ColumnInfo val category_name: String // 카테고리명
)