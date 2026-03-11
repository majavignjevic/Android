package com.example.mycaffe.domain

import java.io.Serializable

/**
 * Data model representing a product/item in the application.
 *
 * <p>This model stores all necessary information about a product such as
 * its title, description, images, price, rating, and cart quantity.</p>
 *
 * <p>The class implements {@link Serializable} so that item objects can
 * be passed between activities using Android intents.</p>
 *
 * @property title Name of the item
 * @property description Detailed description of the item
 * @property picUrl List of image URLs associated with the item
 * @property price Price of the item
 * @property rating Rating of the item (typically between 0.0 and 5.0)
 * @property numberInCart Quantity of this item currently added to the shopping cart
 * @property extra Additional information about the item (for example size or variant)
 */
data class ItemsModel(
    var title: String = "",
    var description: String = "",
    var picUrl: ArrayList<String> = ArrayList(),
    var price: Double = 0.00,
    var rating: Double = 0.0,
    var numberInCart: Int = 0,
    var extra: String = ""
    ): Serializable
