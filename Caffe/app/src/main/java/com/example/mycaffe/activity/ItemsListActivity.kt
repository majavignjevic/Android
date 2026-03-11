package com.example.mycaffe.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycaffe.adapter.ListItemCategoryAdapter
import com.example.mycaffe.databinding.ActivityItemsListBinding
import com.example.mycaffe.viewModel.MainViewModel

/**
 * Activity that displays a grid of items belonging to a selected category.
 *
 * <p>This activity:</p>
 * <ul>
 *     <li>Retrieves category ID and title from intent extras</li>
 *     <li>Fetches the corresponding items using [MainViewModel]</li>
 *     <li>Displays the items in a RecyclerView with a GridLayoutManager</li>
 *     <li>Shows a loading indicator while data is fetched</li>
 * </ul>
 */
class ItemsListActivity : AppCompatActivity() {

    /**
     * ViewBinding instance to access views from {@code activity_items_list.xml}.
     */
    lateinit var binding: ActivityItemsListBinding

    /**
     * ViewModel responsible for fetching items data.
     */
    private val viewModel = MainViewModel()

    /**
     * ID of the selected category passed via intent extras.
     */
    private var id: String = ""

    /**
     * Title of the selected category passed via intent extras.
     */
    private var title: String = ""

    /**
     * Called when the activity is created.
     *
     * <p>Initializes:</p>
     * <ul>
     *     <li>Edge-to-edge UI</li>
     *     <li>ViewBinding</li>
     *     <li>Retrieves category data from intent extras</li>
     *     <li>Sets up the items grid</li>
     * </ul>
     *
     * @param savedInstanceState previous state of the activity if it existed
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables modern edge-to-edge layout
        enableEdgeToEdge()

        // Inflate the layout using ViewBinding
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundles() // Retrieve category ID and title
        initList() // Initialize items RecyclerView
    }

    /**
     * Initializes the RecyclerView that displays items in a grid.
     *
     * <p>This method:</p>
     * <ul>
     *     <li>Shows a loading indicator while items are fetched</li>
     *     <li>Uses a GridLayoutManager with 2 columns</li>
     *     <li>Sets an adapter to populate the items</li>
     *     <li>Handles back button click</li>
     * </ul>
     */
    private fun initList() {
        binding.apply {
            // Show loading indicator while data is being retrieved
            progressBar.visibility = View.VISIBLE

            // Observe items from ViewModel
            viewModel.loadItems(id).observe(this@ItemsListActivity, Observer {
                // Display items in a grid layout with two columns
                listView.layoutManager = GridLayoutManager(this@ItemsListActivity,2)

                // Attach adapter to populate RecyclerView with popular items
                listView.adapter = ListItemCategoryAdapter(it)

                // Hide loading indicator once data is loaded
                progressBar.visibility = View.GONE
            })

            // Finish activity when back button is clicked
            backImg.setOnClickListener { finish() }
        }
    }

    /**
     * Retrieves the category ID and title from intent extras.
     *
     * <p>Also sets the category title in the corresponding TextView.</p>
     */
    private fun getBundles() {
        // Get category ID from intent
        id = intent.getStringExtra("id")!!

        // Get category title from intent
        title = intent.getStringExtra("title")!!

        // Set the category title in the TextView
        binding.categoryTxt.text = title
    }
}