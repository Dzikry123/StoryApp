package com.example.sosmeddicoding.data.model


import com.google.gson.annotations.SerializedName

data class ResponseAddNewStory(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
