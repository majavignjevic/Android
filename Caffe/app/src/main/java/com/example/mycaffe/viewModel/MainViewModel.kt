package com.example.mycaffe.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mycaffe.domain.CategoryModel
import com.example.mycaffe.domain.ItemsModel
import com.example.mycaffe.repository.MainRepository

/**
 * Main view model
 *
 * @constructor Create empty Main view model
 */
class MainViewModel: ViewModel() {
    /** Repository instance to fetch data from Firebase */
    private val repository = MainRepository()

    /**
     * Loads category data from the repository.
     *
     * @return LiveData containing a mutable list of [CategoryModel]
     */
    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    /**
     * Loads popular items data from the repository.
     *
     * @return LiveData containing a mutable list of [ItemsModel]
     */
    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        return repository.loadPopular()
    }

    /**
     * Loads items filtered by a specific category.
     *
     * @param categoryId ID of the category to filter items
     * @return LiveData containing a mutable list of [ItemsModel] for the category
     */
    fun loadItems(categoryId: String): LiveData<MutableList<ItemsModel>>{
        return repository.loadItemCategory(categoryId)
    }
}