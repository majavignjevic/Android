package com.example.mycaffe.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycaffe.adapter.CategoryAdapter
import com.example.mycaffe.adapter.PopularAdapter
import com.example.mycaffe.databinding.ActivityMainBinding
import com.example.mycaffe.viewModel.MainViewModel


/**
 * MainActivity is the entry point of the application.
 *
 * <p>This activity displays:</p>
 * <ul>
 *     <li>A horizontal list of product categories</li>
 *     <li>A grid of popular coffee items</li>
 *     <li>Navigation buttons to the cart and favourites screens</li>
 * </ul>
 *
 * <p>Data for categories and popular items is retrieved using [MainViewModel].</p>
 */
class MainActivity : AppCompatActivity() {
    /**
     * ViewBinding instance used to access views from {@code activity_main.xml}.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * ViewModel responsible for loading category and popular item data.
     */
    private val viewModel = MainViewModel()


    /**
     * Called when the activity is created.
     *
     * <p>Initializes:</p>
     * <ul>
     *     <li>Edge-to-edge UI layout</li>
     *     <li>ViewBinding</li>
     *     <li>Category list</li>
     *     <li>Popular items list</li>
     *     <li>Bottom navigation menu</li>
     * </ul>
     *
     * @param savedInstanceState previous state of the activity if it existed
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables modern edge-to-edge layout
        enableEdgeToEdge()

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI sections
        initCategory()
        initPopular()
        initBottomMenu()
    }

    /**
     * Initializes bottom navigation buttons.
     *
     * <p>Handles navigation to:</p>
     * <ul>
     *     <li>{@link CartActivity} - shopping cart screen</li>
     *     <li>{@link FavouritesActivity} - saved favourite items</li>
     * </ul>
     */
    private fun initBottomMenu() {
        // Open Cart screen when cart button is pressed
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // Open Favourites screen when favourite button is pressed
        binding.favBtn.setOnClickListener {
            startActivity(Intent(this, FavouritesActivity::class.java))
        }
    }

    /**
     * Loads and displays popular coffee items.
     *
     * <p>The data is retrieved from [MainViewModel] and displayed
     * in a RecyclerView using a GridLayoutManager with two columns.</p>
     */
    private fun initPopular() {
        // Show loading indicator while data is being retrieved
        binding.progressBarPopular.visibility = View.VISIBLE

        // Observe data changes from the ViewModel
        viewModel.loadPopular().observeForever {
            // Display items in a grid layout with two columns
            binding.recyclerViewPopular.layoutManager = GridLayoutManager(this, 2)

            // Attach adapter to populate RecyclerView with popular items
            binding.recyclerViewPopular.adapter = PopularAdapter (it)

            // Hide loading indicator once data is loaded
            binding.progressBarPopular.visibility = View.GONE
        }

        // Trigger loading of popular items
        viewModel.loadPopular()
    }

    /**
     * Loads and displays product categories.
     *
     * <p>Categories are shown in a horizontally scrolling RecyclerView.
     * Data is retrieved through [MainViewModel].</p>
     */
    private fun initCategory() {
        // Show loading indicator while categories are loading
        binding.progressBarCategory.visibility = View.VISIBLE

        // Observe category data from ViewModel
        viewModel.loadCategory().observeForever {
            // Set horizontal layout manager for category list
            binding.recyclerViewCategory.layoutManager = LinearLayoutManager(
                this@MainActivity, LinearLayoutManager.HORIZONTAL, false
            )

            // Attach adapter to populate categories
            binding.recyclerViewCategory.adapter = CategoryAdapter(it)

            // Hide loading indicator after loading
            binding.progressBarCategory.visibility = View.GONE
        }

        // Trigger loading of category data
        viewModel.loadCategory()

    }

}