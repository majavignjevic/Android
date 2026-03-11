package com.example.mycaffe.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.mycaffe.Helper.ChangeNumberItemsListener
import com.example.mycaffe.Helper.ManagmentCart
import com.example.mycaffe.databinding.ViewholderCartBinding
import com.example.mycaffe.domain.ItemsModel

/**
 * Adapter for displaying items in the shopping cart RecyclerView.
 *
 * <p>This adapter is responsible for:</p>
 * <ul>
 *     <li>Displaying item title, price, total, quantity, and image</li>
 *     <li>Handling plus, minus, and remove actions for each cart item</li>
 *     <li>Updating the cart via {@link ManagmentCart}</li>
 *     <li>Notifying an external listener when item quantities change</li>
 * </ul>
 *
 * @param listItemSelected List of items currently in the cart
 * @param context Context used to initialize cart management and image loading
 * @param changeNumberItemListener Optional listener to notify external components when cart changes
 */
class CartAdapter (
    private val listItemSelected: ArrayList<ItemsModel>,
    context: Context,
    var changeNumberItemListener: ChangeNumberItemsListener?=null
) : RecyclerView.Adapter<CartAdapter.Viewholder>() {
    /**
     * ViewHolder class that holds the binding for each cart item view.
     *
     * @param binding ViewBinding instance for a single cart item layout
     */
    class Viewholder (val binding: ViewholderCartBinding):
    RecyclerView.ViewHolder(binding.root)

    /**
     *  Manages cart operations such as add, remove, and update quantity
     *  */
    private val managmentCart = ManagmentCart(context)

    /**
     * Inflates the item layout and creates a ViewHolder for the RecyclerView.
     *
     * @param parent ViewGroup into which the new view will be added
     * @param viewType The view type of the new View
     * @return A new ViewHolder holding the inflated item layout
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartAdapter.Viewholder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * <p>Sets:</p>
     * <ul>
     *     <li>Item title, price, total price, and quantity</li>
     *     <li>Item image using Glide with center crop</li>
     *     <li>Click listeners for plus, minus, and remove buttons</li>
     * </ul>
     *
     * @param holder The ViewHolder to bind
     * @param position The position of the item in the list
     */
    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val currentPosition = holder.adapterPosition

        // Safety check for valid position
        if (currentPosition == RecyclerView.NO_POSITION || currentPosition >= listItemSelected.size) return

        val item = listItemSelected[currentPosition]

        // Set item details
        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItemTxt.text = String.format("€%.2f", item.price)
        holder.binding.totalEachItem.text = String.format("€%.2f", item.numberInCart * item.price)
        holder.binding.numberInCartTxt.text = item.numberInCart.toString()

        val imageUrl = item.picUrl?.firstOrNull()

        // Load item image using Glide
        imageUrl?.let {
            Glide.with(holder.itemView.context)
                .load(it)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(holder.binding.picCart)
        }

        // Handle plus button click to increase item quantity
        holder.binding.plusBtn.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                managmentCart.plusItem(listItemSelected, pos, object : ChangeNumberItemsListener{
                    override fun onChanged() {
                        // Refresh RecyclerView
                        notifyDataSetChanged()

                        // Notify external listener
                        changeNumberItemListener?.onChanged()
                    }
                })
            }
        }

        // Handle minus button click to decrease item quantity
        holder.binding.minusBtn.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                managmentCart.minusItem(listItemSelected, pos, object : ChangeNumberItemsListener{
                    override fun onChanged() {
                        // Refresh RecyclerView
                        notifyDataSetChanged()

                        // Notify external listener
                        changeNumberItemListener?.onChanged()
                    }
                })
            }
        }

        // Handle remove button click to remove item from cart
        holder.binding.removeItemBtn.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                managmentCart.removeItem(listItemSelected, pos, object : ChangeNumberItemsListener{
                    override fun onChanged() {
                        // Refresh RecyclerView
                        notifyDataSetChanged()

                        // Notify external listener
                        changeNumberItemListener?.onChanged()
                    }
                })
            }
        }
    }

    /**
     * Returns the total number of items currently in the cart.
     *
     * @return Number of items in the cart
     */
    override fun getItemCount(): Int = listItemSelected.size
}