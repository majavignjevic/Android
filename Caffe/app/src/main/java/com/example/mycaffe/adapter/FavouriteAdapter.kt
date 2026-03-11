package com.example.mycaffe.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycaffe.Helper.ManagmentFavourites
import com.example.mycaffe.activity.DetailActivity
import com.example.mycaffe.databinding.ViewholderFavouriteBinding
import com.example.mycaffe.domain.ItemsModel
import java.util.Locale

/**
 * Adapter responsible for displaying favourite items in a RecyclerView.
 *
 * <p>This adapter:</p>
 * <ul>
 *     <li>Displays the item title, price, and image</li>
 *     <li>Allows users to remove items from favourites</li>
 *     <li>Opens {@link DetailActivity} when an item is selected</li>
 * </ul>
 *
 * @param listItems List of favourite items to display
 * @param context Context used for managing favourites and starting activities
 * @param onChanged Callback triggered when the favourites list is modified
 */
class FavouriteAdapter(
    private val listItems: ArrayList<ItemsModel>,
    context: Context,
    private val onChanged: () -> Unit
) : RecyclerView.Adapter<FavouriteAdapter.Viewholder>() {

    /**
     * Manages favourite items operations like remove
     */
    private val managmentFavourites = ManagmentFavourites(context)

    /**
     * ViewHolder class representing a single favourite item view.
     *
     * @param binding ViewBinding instance for the favourite item layout
     */
    class Viewholder(val binding: ViewholderFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates the favourite item layout and creates a new ViewHolder.
     *
     * @param parent The parent ViewGroup
     * @param viewType The type of view being created
     * @return A new ViewHolder instance
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val binding = ViewholderFavouriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Viewholder(binding)
    }

    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * <p>This includes:</p>
     * <ul>
     *     <li>Setting the item title and formatted price</li>
     *     <li>Loading the item image using Glide</li>
     *     <li>Handling removal of the item from favourites</li>
     *     <li>Opening {@link DetailActivity} when the item is clicked</li>
     * </ul>
     *
     * @param holder The ViewHolder being bound
     * @param position The position of the item in the list
     */
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val item = listItems[position]

        // Set item title and price
        holder.binding.titleTxt.text = item.title
        holder.binding.priceTxt.text = String.format(Locale.US, "€%.2f", item.price)

        // Load item image with Glide
        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .into(holder.binding.picFav)

        // Remove item from favourites when remove button is clicked
        holder.binding.removeFavBtn.setOnClickListener {
            managmentFavourites.removeFavourite(item)
            listItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, listItems.size)
            onChanged()
        }

        // Open DetailActivity when item is clicked
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object", item)
            holder.itemView.context.startActivity(intent)
        }
    }

    /**
     * Returns the total number of favourite items displayed.
     *
     * @return Number of items in the favourites list
     */
    override fun getItemCount(): Int = listItems.size
}
