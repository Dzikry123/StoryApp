package com.example.sosmeddicoding.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "story")
@Parcelize
data class StoryResponseItem(

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("desc")
    val description: String,

    @ColumnInfo(name = "photo_url")
    @field:SerializedName("photo_url")
    val photoUrl: String,

    @ColumnInfo(name = "created_at")
    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("lat")
    val lat: Double?,

    @field:SerializedName("lon")
    val lon: Double?

): Parcelable