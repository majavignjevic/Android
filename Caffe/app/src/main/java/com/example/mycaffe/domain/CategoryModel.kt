package com.example.mycaffe.domain

/**
 * Data model representing a product category.
 *
 * <p>This model is used to group items into categories such as
 * different types of coffee or beverages. Each category contains
 * a title displayed to the user and a unique identifier used
 * for retrieving related items.</p>
 *
 * @property title Name of the category displayed in the UI
 * @property id Unique identifier used to fetch items belonging to the category
 */
data class CategoryModel(val title: String = "", val id: Int = 0)
