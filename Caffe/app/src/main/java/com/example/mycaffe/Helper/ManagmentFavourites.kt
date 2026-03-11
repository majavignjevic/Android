package com.example.mycaffe.Helper

import android.content.Context
import android.widget.Toast
import com.example.mycaffe.domain.ItemsModel

/**
 * Manages favourite items within the application.
 *
 * <p>This class provides functionality for handling user favourites, including:</p>
 * <ul>
 *     <li>Adding items to favourites</li>
 *     <li>Removing items from favourites</li>
 *     <li>Checking if an item is already marked as favourite</li>
 *     <li>Persisting favourite items using {@link TinyDB}</li>
 * </ul>
 *
 * <p>Favourite items are stored locally using {@link TinyDB} so that they
 * remain available even after the application is restarted.</p>
 *
 * @param context Android context used for accessing storage and displaying Toast messages
 */
class ManagmentFavourites(val context: Context) {
    /**
     * TinyDB instance used to persist favourite items locally
     */
    private val tinyDB = TinyDB(context)

    /**
     * Retrieves the list of favourite items currently stored.
     *
     * @return ArrayList containing all favourite items,
     * or an empty list if no favourites are stored
     */
    fun getListFavourites(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("FavouriteList") ?: arrayListOf()
    }

    /**
     * Adds or removes an item from the favourites list.
     *
     * <p>If the item is not already marked as favourite, it will be added.
     * If the item already exists in the favourites list, it will be removed.</p>
     *
     * @param item The item to toggle in the favourites list
     */
    fun addFavourite(item: ItemsModel) {
        val list = getListFavourites()

        // Adding or removing item from favourite
        if (isFavourite(item)) {
            // If the item is already a favourite, remove it
            list.removeAll { it.title == item.title }
        } else {
            // Otherwise, add it to the favourites list
            list.add(item)
        }

        // Save updated favourites list to TinyDB
        tinyDB.putListObject("FavouriteList", list)

        // Show confirmation message to the user
        Toast.makeText(context, "Dodano u favorite", Toast.LENGTH_SHORT).show()
    }

    /**
     * Removes an item completely from the favourites list.
     *
     * @param item The item to remove from favourites
     */
    fun removeFavourite(item: ItemsModel) {
        val list = getListFavourites()

        // Remove all items with matching title
        list.removeAll { it.title == item.title }

        // Save updated list to TinyDB
        tinyDB.putListObject("FavouriteList", list)

        // Show confirmation message
        Toast.makeText(context, "Uklonjeno iz favorita", Toast.LENGTH_SHORT).show()
    }

    /**
     * Checks whether a specific item is currently marked as favourite.
     *
     * <p>The check is performed by comparing item titles stored in the favourites list.</p>
     *
     * @param item The item to check
     * @return {@code true} if the item exists in the favourites list,
     * otherwise {@code false}
     */
    fun isFavourite(item: ItemsModel): Boolean {
        val list = getListFavourites()

        // Check if any item in the list has the same title
        return list.any { it.title == item.title }
    }
}