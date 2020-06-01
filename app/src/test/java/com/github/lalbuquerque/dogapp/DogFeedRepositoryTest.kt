package com.github.lalbuquerque.dogapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.lalbuquerque.dogapp.api.DogApi
import com.github.lalbuquerque.dogapp.api.vo.DogImage
import com.github.lalbuquerque.dogapp.db.dao.UserDao
import com.github.lalbuquerque.dogapp.db.entity.UserEntity
import com.github.lalbuquerque.dogapp.repository.DogFeedRepository
import com.github.lalbuquerque.dogapp.ui.dogfeed.DogCategory
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class DogFeedRepositoryTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val dogApiMock: DogApi = mock()
    val userDaoMock: UserDao = mock()
    val userEntityMock: UserEntity = mock()

    var dogFeedRepository: DogFeedRepository? = null

    @Before
    fun setUpViewModel() {
        dogFeedRepository = DogFeedRepository(dogApiMock, userDaoMock)
    }

    @Test
    fun `given viewmodel called retrieveDogFeed() then repository should call userDaoMock get() to get token`() {
        whenever(userEntityMock.token).thenAnswer { "123" }
        whenever(userDaoMock.get()).thenAnswer { userEntityMock }
        whenever(dogApiMock.getDogFeed(any(), any()))
            .thenAnswer { Observable.just(provideFakeDogImageList()) }

        val testObserver = dogFeedRepository!!.retrieveDogFeed()
            .test()

        verify(userDaoMock).get()
        verify(userEntityMock).token

        testObserver.dispose()
    }

    @Test
    fun `given viewmodel called retrieveDogFeed() then repository should call dogApi getDogFeed()`() {
        whenever(userEntityMock.token).thenAnswer { "123" }
        whenever(userDaoMock.get()).thenAnswer { userEntityMock }
        whenever(dogApiMock.getDogFeed(any(), any()))
            .thenAnswer { Observable.just(provideFakeDogImageList()) }

        val testObserver = dogFeedRepository!!.retrieveDogFeed(DogCategory.HUSKY)
            .test()

        verify(dogApiMock).getDogFeed(any(), any())

        testObserver.dispose()
    }

    @Test
    fun `given viewmodel called retrieveDogFeed() with a single category then repository should call dogApi getDogFeed() once`() {
        whenever(userEntityMock.token).thenAnswer { "123" }
        whenever(userDaoMock.get()).thenAnswer { userEntityMock }
        whenever(dogApiMock.getDogFeed(any(), any()))
            .thenAnswer { Observable.just(provideFakeDogImageList()) }

        val testObserver = dogFeedRepository!!.retrieveDogFeed(DogCategory.HUSKY)
            .test()

        verify(dogApiMock, times(1)).getDogFeed(any(), any())

        testObserver.dispose()
    }

    @Test
    fun `given viewmodel called retrieveDogFeed() without specifying a category then repository should call dogApi getDogFeed() 4 times`() {
        whenever(userEntityMock.token).thenAnswer { "123" }
        whenever(userDaoMock.get()).thenAnswer { userEntityMock }
        whenever(dogApiMock.getDogFeed(any(), any()))
            .thenAnswer { Observable.just(provideFakeDogImageList()) }

        val testObserver = dogFeedRepository!!.retrieveDogFeed()
            .test()

        verify(dogApiMock, times(4)).getDogFeed(any(), any())

        testObserver.dispose()
    }

    @Test
    fun `given viewmodel called retrieveDogFeed() with category ALL then repository should call dogApi getDogFeed() 4 times`() {
        whenever(userEntityMock.token).thenAnswer { "123" }
        whenever(userDaoMock.get()).thenAnswer { userEntityMock }
        whenever(dogApiMock.getDogFeed(any(), any()))
            .thenAnswer { Observable.just(provideFakeDogImageList()) }

        val testObserver = dogFeedRepository!!.retrieveDogFeed(DogCategory.ALL)
            .test()

        verify(dogApiMock, times(4)).getDogFeed(any(), any())

        testObserver.dispose()
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