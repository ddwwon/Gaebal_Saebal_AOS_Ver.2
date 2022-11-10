// 기록 내용 DB Entity(테이블) 정의
package com.example.gaebal_saebal_aos_ver2.db_entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// key, 카테고리(외래키), 본문, 태그, 백준(문제번호), 깃허브(?), 사진, 코드

@Entity (foreignKeys
    = [ForeignKey(
        entity = CategoryDataEntity::class,  // 외래키 연결대상 Entity 클래스
        parentColumns = ["category_uid"],    // 외래키 연결대상 Entity 필드명
        childColumns = ["record_category_uid"],
        onDelete = ForeignKey.CASCADE	     // 삭제될 경우 같이 삭제 설정
)])
data class RecordDataEntity (
    @PrimaryKey(autoGenerate = true)
    val record_uid: Int,          // id = key

    @ColumnInfo
    var record_category_uid: Int, // 카테고리명 DB id - 외래키

    @ColumnInfo
    var record_contents: String,  // 본문

    @ColumnInfo
    var record_tags: MutableIterable<String>, // 태그

    @ColumnInfo
    var record_baekjoon_num: Int, // 백준 문제번호

    //@ColumnInfo
    //var record_github: , // 깃허브

    @ColumnInfo
    var record_image: Bitmap,     // 이미지

    @ColumnInfo
    var record_code: String       // code 내용
)