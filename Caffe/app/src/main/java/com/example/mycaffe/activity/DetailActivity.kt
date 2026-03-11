package com.example.mycaffe.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mycaffe.Helper.ManagmentCart
import com.example.mycaffe.Helper.ManagmentFavourites
import com.example.mycaffe.databinding.ActivityDetailBinding
import com.example.mycaffe.domain.ItemsModel
import com.example.mycaffe.R
import java.util.Locale


/**
 * Activity responsible for displaying detailed information about a selected product.
 *
 * <p>This activity allows the user to:</p>
 * <ul>
 *     <li>View product image, title, description, price, and rating</li>
 *     <li>Select product size</li>
 *     <li>Increase or decrease quantity</li>
 *     <li>Add the product to the shopping cart</li>
 *     <li>Add or remove the product from favourites</li>
 * </ul>
 */
class DetailActivity : AppCompatActivity() {

    /**
     * ViewBinding instance used to access UI components from {@code activity_detail.xml}.
     */
    lateinit var binding: ActivityDetailBinding

    /**
     * Model object representing the selected product.
     */
    private lateinit var item: ItemsModel

    /**
     * Helper class used to manage cart operations such as inserting items.
     */
    private lateinit var managementCart: ManagmentCart

    /** Helper class used to manage favourite items */
    private lateinit var managmentFavourites: ManagmentFavourites

    /**
     * Called when the activity is created.
     *
     * <p>This method initializes:</p>
     * <ul>
     *     <li>Edge-to-edge layout</li>
     *     <li>ViewBinding</li>
     *     <li>Cart and favourites managers</li>
     *     <li>Product data from the intent</li>
     *     <li>Size selection buttons</li>
     * </ul>
     *
     * @param savedInstanceState previous state of the activity, if it existed
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge layout for modern UI appearance
        enableEdgeToEdge()

        // Inflate layout using ViewBinding
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize helper classes
        managementCart = ManagmentCart(this)
        managmentFavourites = ManagmentFavourites(this)

        bundle() // Load product data from intent
        initSizeList() // Initialize size selection buttons
    }

    /**
     * Initializes click listeners for size selection buttons.
     *
     * <p>When a size button is clicked, it receives a highlighted background
     * while the other buttons are cleared.</p>
     */
    private fun initSizeList() {
        binding.apply {
            smallBtn.setOnClickListener {
                smallBtn.setBackgroundResource(R.drawable.brown_stroke_bg)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(0)
            }

            mediumBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(R.drawable.brown_stroke_bg)
                largeBtn.setBackgroundResource(0)
            }

            largeBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(R.drawable.brown_stroke_bg)
            }
        }
    }

    /**
     * Retrieves the selected product passed from another activity
     * and populates the UI with its data.
     *
     * <p>Also sets up button listeners for:</p>
     * <ul>
     *     <li>Adding item to cart</li>
     *     <li>Increasing/decreasing quantity</li>
     *     <li>Toggling favourite status</li>
     *     <li>Returning back to the previous screen</li>
     * </ul>
     */
    @SuppressLint("DefaultLocale")
    private fun bundle() {
        binding.apply {
            // Retrieve product object sent through intent
            item = intent.getSerializableExtra("object") as ItemsModel

            // Load product image using Glide library
            Glide.with(this@DetailActivity)
                .load(item.picUrl[0])
                .into(binding.picMain)

            // Populate UI with product information
            titleBtn.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = String.format(Locale.US, "€%.2f", item.price)
            ratingTxt.text = item.rating.toString()

            // Set initial favourite icon state
            updateFavIcon()

            // Toggle favourite state when favourite button is clicked
            favBtn.setOnClickListener {
                managmentFavourites.addFavourite(item)
                updateFavIcon()
            }

            // Add selected item to the cart
            addToCartBtn.setOnClickListener {
                item.numberInCart = Integer.valueOf(
                    numberInCartTxt.text.toString()
                )
                managementCart.insertItems(item)
            }

            // Close activity and return to previous screen
            backImg.setOnClickListener { finish() }

            // Increase quantity of the item
            plusBtn.setOnClickListener {
                numberInCartTxt.text = (item.numberInCart + 1).toString()
                item.numberInCart++
            }

            // Decrease quantity of the item (not below zero)
            minusBtn.setOnClickListener {
                if (item.numberInCart > 0) {
                    numberInCartTxt.text = (item.numberInCart - 1).toString()
                    item.numberInCart--
                }
            }

        }
    }

    /**
     * Updates the favourite icon depending on whether the current item
     * is marked as favourite.
     *
     * <p>If the item is a favourite:</p>
     * <ul>
     *     <li>The icon is colored (orange)</li>
     * </ul>
     *
     * <p>If the item is not a favourite:</p>
     * <ul>
     *     <li>The icon remains default</li>
     * </ul>
     */
    private fun updateFavIcon() {
        if (managmentFavourites.isFavourite(item)) {
            binding.favBtn.setImageResource(R.drawable.btn_3)
            binding.favBtn.setColorFilter(
                resources.getColor(R.color.orange, theme)
            )
        } else {
            binding.favBtn.setImageResource(R.drawable.btn_3)
            binding.favBtn.clearColorFilter()
        }
    }
}