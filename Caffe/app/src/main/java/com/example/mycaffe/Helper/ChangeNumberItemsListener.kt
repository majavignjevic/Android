package com.example.mycaffe.Helper

/**
 * Listener interface used to notify when the quantity of items in the cart changes.
 *
 * <p>This interface is typically implemented by activities or adapters that
 * need to react when the cart content is updated. For example, it can be used
 * to refresh the total price, update the UI, or trigger other cart-related logic.</p>
 *
 * <p>Common events that trigger this listener include:</p>
 * <ul>
 *     <li>Adding an item to the cart</li>
 *     <li>Removing an item from the cart</li>
 *     <li>Increasing or decreasing the quantity of an item</li>
 * </ul>
 */
interface ChangeNumberItemsListener {

    /**
     * Called when the quantity of items in the cart has changed.
     *
     * <p>Implementations should update any UI elements or calculations
     * that depend on the cart contents.</p>
     */
    fun onChanged()
}