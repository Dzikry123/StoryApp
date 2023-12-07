package com.example.sosmeddicoding.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ResponseGetAllStory(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem> = emptyList(),

	@field:SerializedName("error")
	var error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
data class ListStoryItem(

	@field:SerializedName("id")
	var id: String = "",

	@field:SerializedName("photoUrl")
	var photoUrl: String? = "",

	@field:SerializedName("createdAt")
	var createdAt: String? = "",

	@field:SerializedName("name")
	var name: String? = "",

	@field:SerializedName("description")
	var description: String? = "",

	@field:SerializedName("lon")
	var lon: Double? = 0.0,


	@field:SerializedName("lat")
	var lat: Double? = 0.0
) : Parcelable
