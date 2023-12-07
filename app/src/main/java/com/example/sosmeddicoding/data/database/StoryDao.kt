package com.example.sosmeddicoding.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sosmeddicoding.data.database.entity.StoryResponseItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryResponseItem>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryResponseItem>

    @Query("DELETE FROM story")
     fun deleteAll()
}