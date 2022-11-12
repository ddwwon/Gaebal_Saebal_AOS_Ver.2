// 기록 내용 DB Entity(테이블) 정의
package com.example.gaebal_saebal_aos_ver2.db_entity

import android.graphics.Bitmap
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gaebal_saebal_aos_ver2.githubItem
import java.util.*

// key, 카테고리(외래키), 본문, 태그, 백준(문제번호), 깃허브(?), 사진, 코드, 날짜

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
    var record_tags: String, // 태그 -> 보여줄 때 ;; 단위로 분리 필요

    @ColumnInfo
    var record_baekjoon_num: Int, // 백준 문제번호
    @ColumnInfo
    var record_baekjoon_name: String, // 백준 문제이름

    // github
    @ColumnInfo
    var record_github_type: String, // 깃허브 타입: issue, commit, Pull request
    @ColumnInfo
    var record_github_date: Date, // 깃허브 날짜
    @ColumnInfo
    var record_github_title: String, // 깃허브 제목
    @ColumnInfo
    var record_github_repo: String,  // 깃허브 레포지토리

    @ColumnInfo
    var record_image: Bitmap,     // 이미지 Bitmap
    @ColumnInfo
    var record_image_exist: Boolean,     // 이미지 유무

    @ColumnInfo
    var record_code: String,      // code 내용

    @ColumnInfo
    var record_date: Date         // 작성 날짜
)