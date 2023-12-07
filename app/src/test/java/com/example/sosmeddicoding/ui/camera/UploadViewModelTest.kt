package com.example.sosmeddicoding.ui.camera

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.sosmeddicoding.data.model.ResponseAddNewStory
import com.example.sosmeddicoding.getOrAwaitValue
import com.example.sosmeddicoding.ui.story.StoryRepo
import com.example.sosmeddicoding.utils.ResultViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.File
import com.jraska.livedata.test


class UploadViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepo: StoryRepo

    private lateinit var uploadViewModel: UploadViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        uploadViewModel = UploadViewModel(storyRepo)
    }

    @Test
    fun `Upload new story success`() {
        val dummyFile = createDummyImageFile()
        val description = "Test description"

        val dataUploadResponse = MutableLiveData<ResultViewModel<ResponseAddNewStory>>()
        dataUploadResponse.value = ResultViewModel.Success(ResponseAddNewStory())

        uploadViewModel.uploadImage(dummyFile, description, 0.0, 0.0)

        val observer = dataUploadResponse.test()

        observer.assertHasValue()
        observer.assertValue { it is ResultViewModel.Success<*> }
        observer.assertValue { (it as ResultViewModel.Success<*>).data != 0.0 }
    }

    @Test
    fun `Upload new story failed`() = rule.run {
        val dummyFile = createDummyImageFile()
        val description = "Test description"

        val dataUploadResponse = MutableLiveData<ResultViewModel<ResponseAddNewStory>>()
        dataUploadResponse.value = ResultViewModel.Error("add story failed")

        `when`(storyRepo.uploadImage( dummyFile, description,  0.0, 0.0)).thenReturn(dataUploadResponse)

        val actualUploadResponse = uploadViewModel.uploadImage( dummyFile, description,  0.0, 0.0).getOrAwaitValue()
        verify(storyRepo).uploadImage( dummyFile, description,  0.0, 0.0)


        Assert.assertNotNull(actualUploadResponse)
        Assert.assertTrue(actualUploadResponse is ResultViewModel.Error)
    }

    private fun createDummyImageFile(): File {
        val dummyFile = File("dummy_image_file.jpg")
        return dummyFile
    }
}



