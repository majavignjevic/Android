package com.example.mycaffe.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycaffe.Helper.ChangeNumberItemsListener
import com.example.mycaffe.Helper.ManagmentCart
import com.example.mycaffe.adapter.CartAdapter
import com.example.mycaffe.databinding.ActivityCartBinding
import java.util.Locale


/**
 * Activity responsible for displaying the user's shopping cart.
 *
 * <p>This activity displays:
 * <ul>
 *     <li>All items currently added to the cart</li>
 *     <li>Item subtotal</li>
 *     <li>Tax amount</li>
 *     <li>Delivery fee</li>
 *     <li>Total price</li>
 * </ul>
 * </p>
 *
 * <p>Users can also modify item quantities within the cart, which automatically updates
 * the cart totals.</p>
 */
class CartActivity : AppCompatActivity() {
    /**
     * ViewBinding instance used to access UI elements from {@code activity_cart.xml}.
     */
    lateinit var binding: ActivityCartBinding

    /**
     * Helper class responsible for managing cart operations such as:
     * adding items, removing items, retrieving cart list, and calculating total fees.
     */
    lateinit var managmentCart: ManagmentCart

    /**
     * Stores the calculated tax value for the current cart.
     */
    private var tax: Double = 0.0

    /**
     * Called when the activity is first created.
     *
     * <p>This method initializes:</p>
     * <ul>
     *     <li>ViewBinding</li>
     *     <li>Cart management helper</li>
     *     <li>RecyclerView for cart items</li>
     *     <li>Cart price calculations</li>
     * </ul>
     *
     * @param savedInstanceState previous state of the activity, if it existed
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            // Inflate layout using ViewBinding
            binding = ActivityCartBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Initialize cart manager
            managmentCart = ManagmentCart(this)

            calculateCart() // Calculate cart totals
            setVariable()  // Initialize UI variables and listeners
            initCartList() // Initialize RecyclerView displaying cart items
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Cart init error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Initializes the RecyclerView that displays the list of items in the cart.
     *
     * <p>Uses a {@link LinearLayoutManager} for vertical item display and
     * {@link CartAdapter} to bind cart data to the RecyclerView. A
     * {@link ChangeNumberItemsListener} is used to detect quantity changes and
     * automatically recalculate cart totals.</p>
     */
    private fun initCartList() {
        binding.apply {
            // Set vertical layout manager
            listView.layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)

            // Attach adapter with cart data
            listView.adapter = CartAdapter(
                managmentCart.getListCart(),
                this@CartActivity,
                object: ChangeNumberItemsListener {
                    /**
                     * Called whenever the quantity of a cart item changes.
                     * Recalculates the cart totals.
                     */
                    override fun onChanged() {
                        calculateCart()
                    }
                }
            )
        }
    }

    /**
     * Initializes UI event listeners for the activity.
     *
     * <p>Currently handles:</p>
     * <ul>
     *     <li>Back button click to close the activity</li>
     * </ul>
     */
    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }

    /**
     * Calculates the financial summary of the cart.
     *
     * <p>This method calculates:</p>
     * <ul>
     *     <li>Item subtotal</li>
     *     <li>Tax amount (13%)</li>
     *     <li>Delivery fee</li>
     *     <li>Final total price</li>
     * </ul>
     *
     * <p>After calculation, the values are displayed in the UI.</p>
     */
    @SuppressLint("DefaultLocale")
    private fun calculateCart() {
        // Tax percentage (13%)
        val percentTax = 0.13

        // Fixed delivery cost
        val delivery = 10.00

        // Calculate tax from subtotal
        tax = ((managmentCart.getTotalFee()*percentTax)*100)/100.0

        // Calculate final total price
        val total = ((managmentCart.getTotalFee()+tax+delivery)*100)/100

        // Calculate item subtotal
        val itemTotal = (managmentCart.getTotalFee()*100)/100

        // Update UI with calculated values
        binding.apply{
            totalFeeTxt.text = String.format(Locale.US, "€%.2f", itemTotal)
            totalTaxTxt.text = String.format(Locale.US, "€%.2f", tax)
            deliveryFeeTxt.text = String.format(Locale.US, "€%.2f", delivery)
            totalTxt.text = String.format(Locale.US, "€%.2f", total)
        }
    }
}