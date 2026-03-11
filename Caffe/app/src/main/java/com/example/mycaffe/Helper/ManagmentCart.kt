package com.example.mycaffe.Helper

import android.content.Context
import android.widget.Toast
import com.example.mycaffe.domain.ItemsModel

import kotlin.collections.indexOfFirst

/**
 * Manages all shopping cart operations within the application.
 *
 * <p>This class is responsible for handling cart-related logic such as:</p>
 * <ul>
 *     <li>Adding items to the cart</li>
 *     <li>Removing items from the cart</li>
 *     <li>Increasing or decreasing item quantity</li>
 *     <li>Persisting cart data using {@link TinyDB}</li>
 *     <li>Calculating the total price of items in the cart</li>
 * </ul>
 *
 * <p>Cart data is stored locally using {@link TinyDB}, allowing the cart
 * to persist even after the application is restarted.</p>
 *
 * @param context Android context used for storage access and displaying Toast messages
 */
class ManagmentCart(val context: Context) {

    /**
     * TinyDB instance used to persist cart data locally
     */
    private val tinyDB = TinyDB(context)

    /**
     * Adds a new item to the cart or updates the quantity if the item already exists.
     *
     * <p>If an item with the same title is already present in the cart,
     * its quantity will be updated instead of creating a duplicate entry.</p>
     *
     * @param item The item to add or update in the cart
     */
    fun insertItems(item: ItemsModel) {
        // Retrieve current cart list
        var listItem = getListCart()

        // Check if item exists
        val existAlready = listItem.any { it.title == item.title }

        // Find item index
        val index = listItem.indexOfFirst { it.title == item.title }

        if (existAlready) {
            // Update quantity for existing item
            listItem[index].numberInCart = item.numberInCart
        } else {
            // Add new item to cart
            listItem.add(item)
        }

        // Save updated list to TinyDB
        tinyDB.putListObject("CartList", listItem)

        // Show confirmation message
        Toast.makeText(context, "Dodano u košaricu", Toast.LENGTH_SHORT).show()
    }

    /**
     * Retrieves the current list of items stored in the cart.
     *
     * @return ArrayList containing all items currently in the cart
     */
    fun getListCart(): ArrayList<ItemsModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    /**
     * Decreases the quantity of a specific item in the cart.
     *
     * <p>If the item quantity becomes zero, the item is completely removed
     * from the cart.</p>
     *
     * @param listItems Current list of items in the cart
     * @param position Position of the item to decrease
     * @param listener Listener notified when cart contents change
     */
    fun minusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {

        // Check if the item has only 1 in quantity
        if (listItems[position].numberInCart == 1) {
            // Remove the item completely from the cart
            listItems.removeAt(position)
        } else {
            // Decrease the quantity by 1
            listItems[position].numberInCart--
        }

        // Save updated cart list to TinyDB (persistent storage)
        tinyDB.putListObject("CartList", listItems)

        // Notify listener so UI or totals can be updated
        listener.onChanged()
    }

    /**
     * Removes an item completely from the cart.
     *
     * @param listItems Current cart item list
     * @param position Position of the item to remove
     * @param listener Listener notified when cart contents change
     */
    fun removeItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        // Remove the item from the list
        listItems.removeAt(position)

        // Update TinyDB to persist the change
        tinyDB.putListObject("CartList", listItems)

        // Notify listener so UI or totals can be updated
        listener.onChanged()

        // Show a Toast message to confirm removal to the user
        Toast.makeText(context, "Uklonjeno iz košarice", Toast.LENGTH_SHORT).show()
    }

    /**
     * Increases the quantity of a specific item in the cart.
     *
     * @param listItems Current cart item list
     * @param position Position of the item to increase
     * @param listener Listener notified when cart contents change
     */
    fun plusItem(listItems: ArrayList<ItemsModel>, position: Int, listener: ChangeNumberItemsListener) {
        // Increase the quantity of the selected item
        listItems[position].numberInCart++

        // Update TinyDB to persist the new quantity
        tinyDB.putListObject("CartList", listItems)

        // Notify listener so UI or totals can be updated
        listener.onChanged()
    }

    /**
     * Calculates the total price of all items currently in the cart.
     *
     * <p>The total is calculated by multiplying the price of each item
     * by its quantity and summing all results.</p>
     *
     * @return Total cart price
     */
    fun getTotalFee(): Double {
        // Get current cart items
        val listItem = getListCart()
        var fee = 0.0

        // Loop through each item and multiply price by quantity
        for (item in listItem) {
            fee += item.price * item.numberInCart
        }
        return fee
    }
}