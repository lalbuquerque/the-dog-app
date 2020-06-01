package com.github.lalbuquerque.dogapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.lalbuquerque.dogapp.api.vo.DogImage
import com.github.lalbuquerque.dogapp.repository.DogFeedRepository
import com.github.lalbuquerque.dogapp.ui.dogfeed.DogCategory
import com.github.lalbuquerque.dogapp.ui.dogfeed.DogFeedViewModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class DogFeedViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    var feedRepositoryMock: DogFeedRepository = mock()
    var dogFeedViewModel: DogFeedViewModel? = null

    @Before
    fun setUpViewModel() {
        dogFeedViewModel =
            DogFeedViewModel(feedRepositoryMock, Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    fun `given view called selectCategory() then viewmodel should call repository retrieveDogFeed()`() {
        whenever(feedRepositoryMock.retrieveDogFeed())
            .thenAnswer { Observable.just(provideFakeDogImageList()) }

        dogFeedViewModel!!.selectCategory(DogCategory.ALL)

        verify(feedRepositoryMock).retrieveDogFeed()
    }

    @Test
    fun `given view called selectCategory() with a category then viewmodel should call repository retrieveDogFeed() with same category`() {
        whenever(feedRepositoryMock.retrieveDogFeed(any()))
            .thenAnswer { Observable.just(provideFakeDogImageList()) }

        dogFeedViewModel!!.selectCategory(DogCategory.HOUND)

        val dogCategoryArgumentCaptor = argumentCaptor<DogCategory>()
        verify(feedRepositoryMock).retrieveDogFeed(dogCategoryArgumentCaptor.capture())

        assertEquals(DogCategory.HOUND, dogCategoryArgumentCaptor.firstValue)
    }

    @Test
    fun `given view called selectCategory() with a category then dogCategorySelectionLiveData should be updated with it`() {
        whenever(feedRepositoryMock.retrieveDogFeed(any()))
            .thenAnswer { Observable.just(provideFakeDogImageList()) }

        dogFeedViewModel!!.selectCategory(DogCategory.PUG)

        assertEquals(DogCategory.PUG, dogFeedViewModel!!.dogCategorySelectionLiveData.value!!.selected)
    }

    @Test
    fun `given repository emits error on getDogFeed() then dogFeedLoadResultLiveData should point to error`() {
        whenever(feedRepositoryMock.retrieveDogFeed())
            .thenAnswer { Observable.error<List<DogImage>>(Throwable("")) }

        dogFeedViewModel!!.selectCategory(DogCategory.ALL)

        assertEquals(false, dogFeedViewModel!!.dogFeedLoadResultLiveData.value!!.success)
        assertNotNull(dogFeedViewModel!!.dogFeedLoadResultLiveData.value!!.error)
    }

    @Test
    fun `given repository emits valid data on getDogFeed() then dogFeedLoadResultLiveData should point to success`() {
        whenever(feedRepositoryMock.retrieveDogFeed())
            .thenAnswer { Observable.just(provideFakeDogImageList()) }

        dogFeedViewModel!!.selectCategory(DogCategory.ALL)

        assertEquals(true, dogFeedViewModel!!.dogFeedLoadResultLiveData.value!!.success)
        assertNull(dogFeedViewModel!!.dogFeedLoadResultLiveData.value!!.error)
    }

    @Test
    fun `given repository emits valid data on getDogFeed() then dogFeedLiveData should be filled with it`() {
        val fakeDogImageList = provideFakeDogImageList()

        whenever(feedRepositoryMock.retrieveDogFeed())
            .thenAnswer { Observable.just(fakeDogImageList) }

        dogFeedViewModel!!.selectCategory(DogCategory.ALL)
        dogFeedViewModel!!.dogFeedLiveData.value!!.forEach {
            assertTrue(fakeDogImageList.contains(it.dogImage))
        }
    }

    private fun provideFakeDogImageList(): List<DogImage> {
        return listOf(DogImage("a"),
            DogImage("b"),
            DogImage("c"),
            DogImage("d"),
            DogImage("e"),
            DogImage("f")
        )
    }
}