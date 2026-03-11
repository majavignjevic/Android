package com.example.mycaffe.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycaffe.activity.DetailActivity
import com.example.mycaffe.databinding.ViewholderPopularBinding
import com.example.mycaffe.domain.ItemsModel
import java.util.Locale

/**
 * Adapter responsible for displaying popular items in a RecyclerView.
 *
 * <p>This adapter:</p>
 * <ul>
 *     <li>Displays the item title, subtitle (extra information), price, and image</li>
 *     <li>Loads item images using Glide</li>
 *     <li>Handles click events to open {@link DetailActivity}</li>
 * </ul>
 *
 * @param items List of popular items to display
 */
class PopularAdapter(val items: MutableList<ItemsModel>):
RecyclerView.Adapter<PopularAdapter.Viewholder>(){

    /**
     * Context for starting activities and loading images
     */
    lateinit var context: Context

    /**
     * ViewHolder class representing a single popular item view.
     *
     * @param binding ViewBinding instance for the popular item layout
     */
    class Viewholder (val binding: ViewholderPopularBinding):
    RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates the popular item layout and creates a ViewHolder.
     *
     * @param parent The parent ViewGroup
     * @param viewType The type of view being created
     * @return A new ViewHolder instance
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderPopularBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * <p>This includes:</p>
     * <ul>
     *     <li>Setting the item title</li>
     *     <li>Setting the subtitle (extra information)</li>
     *     <li>Formatting and displaying the item price</li>
     *     <li>Loading the item image using Glide</li>
     *     <li>Opening {@link DetailActivity} when the item is clicked</li>
     * </ul>
     *
     * @param holder The ViewHolder being bound
     * @param position Position of the item in the list
     */
    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: PopularAdapter.Viewholder, position: Int) {
        // Set item title, subtitle, and price
        holder.binding.titleTxt.text = items[position].title
        holder.binding.subtitleTxt.text = items[position].extra.toString()
        holder.binding.priceTxt.text = String.format(Locale.US, "€%.2f", items[position].price)

        // Load item image using Glide
        Glide.with(context)
            .load(items[position].picUrl[0])
            .into(holder.binding.pic)

        // Open DetailActivity when item is clicked
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("object", items[position])  // Pass the item object
            context.startActivity(intent)
        }
    }

    /**
     * Returns the total number of popular items.
     *
     * @return Number of items displayed in the RecyclerView
     */
    override fun getItemCount(): Int = items.size
}