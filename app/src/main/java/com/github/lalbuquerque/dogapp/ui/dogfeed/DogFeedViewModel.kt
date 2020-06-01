package com.github.lalbuquerque.dogapp.ui.dogfeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.lalbuquerque.dogapp.R
import com.github.lalbuquerque.dogapp.api.vo.DogImage
import com.github.lalbuquerque.dogapp.di.qualifiers.ObserveOn
import com.github.lalbuquerque.dogapp.di.qualifiers.SubscribeOn
import com.github.lalbuquerque.dogapp.repository.DogFeedRepository
import com.github.lalbuquerque.dogapp.ui.dogfeed.recyclerview.DogItem
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.io.IOException
import javax.inject.Inject

class DogFeedViewModel @Inject constructor(private val dogFeedRepository: DogFeedRepository,
                                           @SubscribeOn private val subscribeOnScheduler: Scheduler,
                                           @ObserveOn private val observeOnScheduler: Scheduler) :
    ViewModel() {

    private val _dogCategorySelectionLiveData = MutableLiveData<DogCategorySelection>()
    val dogCategorySelectionLiveData: LiveData<DogCategorySelection>
        get() = _dogCategorySelectionLiveData

    private val _dogFeedRetrievalResultLiveData = MutableLiveData<DogFeedLoadResult>()
    val dogFeedLoadResultLiveData: LiveData<DogFeedLoadResult>
        get() = _dogFeedRetrievalResultLiveData

    private val _dogFeedLiveData = MutableLiveData<List<DogItem>>()
    val dogFeedLiveData: LiveData<List<DogItem>>
        get() = _dogFeedLiveData

    fun selectCategory(category: DogCategory) {
        _dogCategorySelectionLiveData.value = DogCategorySelection(category)

        getDogFeed(category)
    }

    private fun getDogFeed(category: DogCategory = DogCategory.ALL) {
        dogFeedRepository.retrieveDogFeed(category)
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
            .subscribeBy(
                onNext = {
                    _dogFeedRetrievalResultLiveData.value = DogFeedLoadResult(success = true,
                        category = category)
                    _dogFeedLiveData.value = buildDogItemList(it)
                },
                onError = {
                    _dogFeedRetrievalResultLiveData.value = DogFeedLoadResult(success = false,
                        error = getDogFeedErrorString(it), category = category)
                },
                onComplete = {}
            )
    }

    private fun buildDogItemList(dogImageList: List<DogImage>): MutableList<DogItem> {
        val newList = mutableListOf<DogItem>()
        dogImageList.forEach { newList.add(DogItem(it)) }
        return newList
    }

    private fun getDogFeedErrorString(throwable: Throwable): Int {
        return when (throwable) {
            is IOException -> R.string.network_error
            else -> R.string.generic_error
        }
    }
}