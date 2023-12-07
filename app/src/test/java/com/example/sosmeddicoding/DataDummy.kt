package com.example.sosmeddicoding


import com.example.sosmeddicoding.data.database.entity.StoryResponseItem
import com.example.sosmeddicoding.data.model.LoginResult
import com.example.sosmeddicoding.data.model.ResponseLogin

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryResponseItem> {
        val items: MutableList<StoryResponseItem> = arrayListOf()
        for (i in 0..100) {
            val story = StoryResponseItem(
                i.toString(),
                "author + $i",
                "story $i",
                "photoUrl $i",
                "createdAt $i",
                0.0,
                0.0,
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyLoginResponse(): ResponseLogin {
        val resLogin = ResponseLogin(
            error = false,
            message = "Success",
            loginResult = LoginResult(
                userId = "user-iHnwvekPEbUHWDcB",
                name = "lupin",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWlIbnd2ZWtQRWJVSFdEY0IiLCJpYXQiOjE2OTgyMTIwMDV9.bwDeTjm7MRMw7O2uSLtlp8OJJ7Atk4yGRfPiZ0pmfLw"
            )
        )

        return resLogin
    }

}