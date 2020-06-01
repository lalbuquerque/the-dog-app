package com.github.lalbuquerque.dogapp.ui.dogfeed

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.lalbuquerque.dogapp.DogApplication
import com.github.lalbuquerque.dogapp.R
import com.github.lalbuquerque.dogapp.extensions.enableAndSelectable
import com.github.lalbuquerque.dogapp.ui.dogfeed.recyclerview.DogFeedAdapter
import com.robertlevonyan.views.chip.OnSelectClickListener
import kotlinx.android.synthetic.main.content_dogs_activity.*
import kotlinx.android.synthetic.main.dog_list_layout.*
import javax.inject.Inject

class DogFeedActivity : AppCompatActivity() {

    @Inject
    lateinit var dogFeedViewModel: DogFeedViewModel
    var dogFeedAdapter: DogFeedAdapter? = DogFeedAdapter(this, emptyList())
    private var lastFirstVisiblePosition: Int = 0
    private var lastFirstVisiblePositionTop: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dogs)

        DogApplication.component?.inject(this)
        dogFeedViewModel.selectCategory(DogCategory.ALL)
    }

    override fun onStart() {
        super.onStart()

        setUpListeners()
        setUpLiveDataObservers()
        setUpScrollViewBars()
    }

    override fun onResume() {
        super.onResume()

        restoreRecyclerViewPosition()
    }

    override fun onPause() {
        super.onPause()

        saveRecyclerViewPosition()
        stopShimmerAnimation()
    }

    private fun restoreRecyclerViewPosition() {
        rvDogImages.layoutManager?.let {
            (it as LinearLayoutManager).scrollToPositionWithOffset(
                lastFirstVisiblePosition,
                lastFirstVisiblePositionTop
            )
        }
    }

    private fun saveRecyclerViewPosition() {
        rvDogImages.layoutManager?.let {
            lastFirstVisiblePosition = (it as LinearLayoutManager)
                .findFirstVisibleItemPosition()
            val v: View? = rvDogImages.getChildAt(0)
            lastFirstVisiblePositionTop = if (v == null) 0 else v.top - rvDogImages.paddingTop
        }
    }

    private fun setUpLiveDataObservers() {
        dogFeedViewModel.dogCategorySelectionLiveData.observe(this@DogFeedActivity, Observer {
            startShimmerAnimation()

            deselectOtherCategoriesThan(it.selected)
        })

        dogFeedViewModel.dogFeedLoadResultLiveData.observe(this@DogFeedActivity, Observer {
            if (it.success) {
                deselectOtherCategoriesThan(it.category ?: DogCategory.ALL)
            } else {
                handleCategorySelectionError(it.error, it.category)
            }
        })

        dogFeedViewModel.dogFeedLiveData.observe(this@DogFeedActivity, Observer {
            stopShimmerAnimation()

            dogFeedAdapter = DogFeedAdapter (this, it)
            rvDogImages.layoutManager = LinearLayoutManager(this)
            rvDogImages.adapter = dogFeedAdapter
            restoreRecyclerViewPosition()
        })
    }

    private fun stopShimmerAnimation() {
        shimmerContainer.stopShimmerAnimation()
        shimmerContainer.visibility = View.INVISIBLE
        rvDogImages.visibility = View.VISIBLE
    }

    private fun startShimmerAnimation() {
        shimmerContainer.startShimmerAnimation()
        shimmerContainer.visibility = View.VISIBLE
        rvDogImages.visibility = View.INVISIBLE
    }

    private fun setUpListeners() {
        tagHusky.onSelectClickListener = onSelectClickListenerFor(DogCategory.HUSKY)
        tagHound.onSelectClickListener = onSelectClickListenerFor(DogCategory.HOUND)
        tagPug.onSelectClickListener = onSelectClickListenerFor(DogCategory.PUG)
        tagLabrador.onSelectClickListener = onSelectClickListenerFor(DogCategory.LABRADOR)
    }

    private fun onSelectClickListenerFor(category: DogCategory): OnSelectClickListener {
        return OnSelectClickListener { v, selected ->
            val selectedCategory = if (selected) category else DogCategory.ALL
            dogFeedViewModel.selectCategory(selectedCategory)
            resetRecyclerViewPositionControl()
        }
    }

    private fun resetRecyclerViewPositionControl() {
        lastFirstVisiblePosition = 0
        lastFirstVisiblePositionTop = 0
    }

    private fun deselectOtherCategoriesThan(category: DogCategory) {
        if (category == DogCategory.ALL) {
            setAllCategoriesEnabled(allTagsUnselected = true)
            return
        }

        setAllCategoriesEnabled(allTagsUnselected = false)

        if (category != DogCategory.HUSKY) tagHusky.chipSelected = false
        if (category != DogCategory.HOUND) tagHound.chipSelected = false
        if (category != DogCategory.PUG) tagPug.chipSelected = false
        if (category != DogCategory.LABRADOR) tagLabrador.chipSelected = false
    }

    private fun setAllCategoriesEnabled(allTagsUnselected: Boolean) {
        tagHusky.enableAndSelectable()
        tagHound.enableAndSelectable()
        tagPug.enableAndSelectable()
        tagLabrador.enableAndSelectable()

        if (allTagsUnselected) {
            tagHusky.chipSelected = false
            tagHound.chipSelected = false
            tagPug.chipSelected = false
            tagLabrador.chipSelected = false
        }
    }

    private fun setUpScrollViewBars() {
        svTabs.isHorizontalScrollBarEnabled = false
        svTabs.isVerticalScrollBarEnabled = false
    }

    private fun handleCategorySelectionError(error: Int?, category: DogCategory?) {
        //TODO: handle error
        deselectOtherCategoriesThan(category ?: DogCategory.ALL)
    }
}
