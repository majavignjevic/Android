package com.example.mycaffe.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mycaffe.domain.CategoryModel
import com.example.mycaffe.domain.ItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener


/**
 * MainRepository handles all Firebase Realtime Database operations for the app.
 *
 * Responsibilities:
 * - Fetch categories from "Category" node
 * - Fetch popular items from "Popular" node
 * - Fetch items filtered by category from "Items" node
 *
 * Uses LiveData so UI can observe changes automatically.
 */
class MainRepository {

    /** Firebase database instance with custom URL */
    private val firebaseDatabase =
        FirebaseDatabase.getInstance("https://mycaffeandroidapp-default-rtdb.europe-west1.firebasedatabase.app")

    /**
     * Loads all product categories from Firebase.
     *
     * @return LiveData containing a mutable list of [CategoryModel]
     */
    fun loadCategory (): LiveData<MutableList<CategoryModel>> {
        // LiveData to hold categories
        val listData = MutableLiveData<MutableList<CategoryModel>>()

        //Reference to "Category" node
        val ref = firebaseDatabase.getReference("Category")

        ref.addValueEventListener(object : ValueEventListener {

            // Called when data is successfully loaded or updated
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<CategoryModel>()

                // Loop through all children of "Category"
                for (childSnapshot in snapshot.children){
                    // Convert to CategoryModel
                    val item = childSnapshot.getValue(CategoryModel::class.java)

                    // add non-null items to list
                    item?.let { list.add(it) }
                }
                listData.value = list
            }

            // Called if there is an error retrieving data
            override fun onCancelled(error: DatabaseError) {
                Log.e("MainRepository", "Failed to load category: ${error.message}")
            }

        })

        return listData
    }

    /**
     * Loads popular items from Firebase.
     *
     * @return LiveData containing a mutable list of [ItemsModel]
     */
    fun loadPopular (): LiveData<MutableList<ItemsModel>> {
        // LiveData for popular items
        val listData = MutableLiveData<MutableList<ItemsModel>>()

        // Reference to "Popular" node
        val ref = firebaseDatabase.getReference("Popular")

        ref.addValueEventListener(object : ValueEventListener {
            // Called when data is successfully loaded or updated
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ItemsModel>()

                // Loop through all popular items
                for (childSnapshot in snapshot.children){
                    // Convert snapshot to ItemsModel
                    val item = childSnapshot.getValue(ItemsModel::class.java)

                    // add non-null items to list
                    item?.let { list.add(it) }
                }
                // Update LiveData so observers can refresh UI
                listData.value = list
            }

            // Called if there is an error retrieving data
            override fun onCancelled(error: DatabaseError) {
                Log.e("MainRepository", "Failed to load popular items: ${error.message}")
            }

        })

        return listData
    }

    /**
     * Loads items for a specific category from Firebase.
     *
     * @param categoryId ID of the category to filter items
     * @return LiveData containing a mutable list of [ItemsModel] for the category
     */
    fun loadItemCategory (categoryId: String): LiveData<MutableList<ItemsModel>> {
        // LiveData for filtered items
        val itemsLiveData = MutableLiveData<MutableList<ItemsModel>> ()

        // Reference to "Items" node
        val ref = firebaseDatabase.getReference("Items")

        // Query items where "categoryId" equals the provided categoryId
        val query: Query = ref.orderByChild("categoryId").equalTo(categoryId)

        query.addListenerForSingleValueEvent(object: ValueEventListener {
            // Called when data is successfully loaded or updated
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ItemsModel>()

                // Loop through all matching items
                for(childSnapshot in snapshot.children){
                    // Convert snapshot to ItemsModel
                   val item = childSnapshot.getValue(ItemsModel::class.java)

                    // add non-null items to list
                    item?.let { list.add(it) }
                }

                // Update LiveData with items for this category
                itemsLiveData.value = list
            }

            // Called if there is an error retrieving data
            override fun onCancelled(error: DatabaseError) {
                Log.e("MainRepository", "Failed to load items for category $categoryId: ${error.message}")
            }



        })

        return itemsLiveData
    }
}