package com.example.sosmeddicoding.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sosmeddicoding.data.database.entity.StoryResponseItem
import com.example.sosmeddicoding.data.service.ApiService

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, StoryResponseItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponseItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllStories(position, params.loadSize)
            val stories = responseData.listStory

            // Melakukan pemetaan dari ListStoryItem ke StoryResponseItem
            val mappedStories = stories.map { listStoryItem ->
                StoryResponseItem(
                    id = listStoryItem?.id!!,
                    name = listStoryItem?.name!!,
                    description = listStoryItem?.description!!,
                    createdAt = listStoryItem?.createdAt!!,
                    photoUrl = listStoryItem?.photoUrl!!,
                    lon = listStoryItem?.lon,
                    lat = listStoryItem?.lat
                )
            }

            LoadResult.Page(
                data = mappedStories,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (stories.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

}


