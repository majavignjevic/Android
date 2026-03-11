package com.example.mycaffe.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycaffe.Helper.ManagmentFavourites
import com.example.mycaffe.adapter.FavouriteAdapter
import com.example.mycaffe.databinding.ActivityFavouritesBinding

/**
 * Activity responsible for displaying the list of the user's favourite items.
 *
 * <p>This activity:</p>
 * <ul>
 *     <li>Retrieves favourite products stored in the application</li>
 *     <li>Displays them using a RecyclerView</li>
 *     <li>Shows a message when the favourites list is empty</li>
 *     <li>Allows users to remove items from favourites via the adapter</li>
 * </ul>
 */
class FavouritesActivity : AppCompatActivity() {
    /**
     * ViewBinding instance used to access UI elements from {@code activity_favourites.xml}.
     */
    private lateinit var binding: ActivityFavouritesBinding

    /**
     * Helper class responsible for managing favourite items.
     */
    private lateinit var managmentFavourites: ManagmentFavourites

    /**
     * Called when the activity is first created.
     *
     * <p>Initializes:</p>
     * <ul>
     *     <li>Edge-to-edge layout</li>
     *     <li>ViewBinding</li>
     *     <li>Favourite manager</li>
     *     <li>Back button navigation</li>
     *     <li>Loading the favourites list</li>
     * </ul>
     *
     * @param savedInstanceState previous state of the activity if it existed
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables modern edge-to-edge layout for the activity
        enableEdgeToEdge()

        // Inflate the layout using ViewBinding
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize favourites management helper
        managmentFavourites = ManagmentFavourites(this)

        // Close the activity when the back button is pressed
        binding.backBtn.setOnClickListener { finish() }

        // Load favourites when activity starts
        loadFavourites()
    }

    /**
     * Called when the activity returns to the foreground.
     *
     * <p>Ensures the favourites list is refreshed if changes were made while
     * the activity was not visible.</p>
     */
    override fun onResume() {
        super.onResume()
        loadFavourites()
    }

    /**
     * Loads favourite items and displays them in the RecyclerView.
     *
     * <p>If the list is empty:</p>
     * <ul>
     *     <li>Shows a message indicating there are no favourites</li>
     *     <li>Hides the RecyclerView</li>
     * </ul>
     *
     * <p>If the list contains items:</p>
     * <ul>
     *     <li>Hides the empty message</li>
     *     <li>Displays the items using FavouriteAdapter</li>
     * </ul>
     */
    private fun loadFavourites() {
        // Retrieve the list of favourite items
        val list = managmentFavourites.getListFavourites()

        if (list.isEmpty()) {
            // Show empty message if there are no favourites
            binding.emptyTxt.visibility = View.VISIBLE
            binding.listView.visibility = View.GONE
        } else {
            // Show the list of favourites
            binding.emptyTxt.visibility = View.GONE
            binding.listView.visibility = View.VISIBLE

            // Set vertical layout for RecyclerView
            binding.listView.layoutManager = LinearLayoutManager(this)

            // Attach adapter to display favourite items
            binding.listView.adapter = FavouriteAdapter(list, this) {
                // Reload favourites when changes occur (e.g. item removed)
                loadFavourites()
            }
        }
    }
}
