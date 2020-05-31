package com.github.lalbuquerque.dogapp.repository

import com.github.lalbuquerque.dogapp.api.DogApi
import com.github.lalbuquerque.dogapp.api.vo.DogImage
import com.github.lalbuquerque.dogapp.db.dao.UserDao
import com.github.lalbuquerque.dogapp.extensions.transformIntoDogImageList
import com.github.lalbuquerque.dogapp.ui.dogfeed.DogCategory
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class DogFeedRepository @Inject constructor(private val dogApi: DogApi,
                                            private val userDao: UserDao) {

    fun retrieveDogFeed(category: DogCategory = DogCategory.ALL): Observable<List<DogImage>> {
        val token = userDao.get().token

        if (category == DogCategory.ALL) {
            return retrieveAllDogBreed(token!!)
        }

        return getApiObservableForCategory(category, token)
            .concatWith(getApiObservableForCategory(category, token))
    }

    private fun retrieveAllDogBreed(token: String): Observable<List<DogImage>> {
        return Observable.merge(getApiObservableForCategory(DogCategory.HUSKY, token),
            getApiObservableForCategory(DogCategory.HOUND, token),
            getApiObservableForCategory(DogCategory.PUG, token),
            getApiObservableForCategory(DogCategory.LABRADOR, token))
            .buffer(4)
            .map { listsOfDogImages ->
                val dogImages = mutableListOf<DogImage>()
                listsOfDogImages.forEach { dogImages.addAll(it) }
                dogImages.shuffled()
            }
    }

    private fun getApiObservableForCategory(category: DogCategory, token: String?): Observable<List<DogImage>> {
        return dogApi.getDogFeed(token!!, getDogCategoryApiParameterText(category))
            .map { it.list.transformIntoDogImageList() }
    }

    private fun getDogCategoryApiParameterText(category: DogCategory): String {
        return when (category) {
            DogCategory.HUSKY -> "husky"
            DogCategory.HOUND -> "hound"
            DogCategory.PUG -> "pug"
            DogCategory.LABRADOR -> "labrador"
            else -> "husky"
        }
    }
}