package com.example.mycaffe.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mycaffe.R
import com.example.mycaffe.activity.ItemsListActivity
import com.example.mycaffe.databinding.ViewholderCategoryBinding
import com.example.mycaffe.domain.CategoryModel

/**
 * Adapter for displaying product categories in a horizontal RecyclerView.
 *
 * <p>This adapter is responsible for:</p>
 * <ul>
 *     <li>Displaying category titles</li>
 *     <li>Highlighting the selected category</li>
 *     <li>Handling click events to navigate to [ItemsListActivity]</li>
 * </ul>
 *
 * @param items List of categories to display
 */
class CategoryAdapter(val items: MutableList<CategoryModel>): RecyclerView.Adapter<CategoryAdapter.Viewholder>() {

    /**
     *  Context used to start activities and access resources
     */
    private lateinit var context: Context

    /**
     * Position of the currently selected category
     */
    private var selectedPosition = -1

    /**
     * Position of the previously selected category
     */
    private var lastSelectedPosition = -1

    /**
     * ViewHolder class holding the binding for a single category item.
     *
     * @param binding ViewBinding for category item layout
     */
    inner class Viewholder(val binding: ViewholderCategoryBinding):
        RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates the category item layout and creates a ViewHolder.
     *
     * @param parent ViewGroup into which the new view will be added
     * @param viewType The type of the new view
     * @return A new ViewHolder holding the inflated layout
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * <p>This includes:</p>
     * <ul>
     *     <li>Setting the category title text</li>
     *     <li>Handling click events to navigate to [ItemsListActivity]</li>
     *     <li>Updating UI to highlight the selected category</li>
     * </ul>
     *
     * @param holder The ViewHolder to bind
     * @param position The position of the item in the list
     */
    override fun onBindViewHolder(holder: CategoryAdapter.Viewholder, position: Int) {
        val item = items[position]

        // Set the category title text
        holder.binding.titleCat.text = item.title

        // Handle click on the category item
        holder.binding.root.setOnClickListener {
            // Update selected and last selected positions
            lastSelectedPosition = selectedPosition
            selectedPosition = position

            // Refresh previous and current items to update highlight
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)

            // Delay navigation slightly for UI feedback
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent (context, ItemsListActivity::class.java).apply {
                    putExtra ("id", item.id.toString())
                    putExtra ("title", item.title)
                }
                ContextCompat.startActivity(context, intent, null)
            },500)
        }

        // Highlight selected category or reset background for others
        if(selectedPosition == position){
            holder.binding.titleCat.setBackgroundResource(R.drawable.brown_full_corner_bg)
            holder.binding.titleCat.setTextColor(context.resources.getColor(R.color.white))
        } else {
            holder.binding.titleCat.setBackgroundResource(R.drawable.white_full_corner_bg)
            holder.binding.titleCat.setTextColor(context.resources.getColor(R.color.darkBrown))
        }
    }

    /**
     * Returns the total number of category items.
     *
     * @return Number of categories in the list
     */
    override fun getItemCount(): Int = items.size
}