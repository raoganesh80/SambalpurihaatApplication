package com.inces.incesclient.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inces.incesclient.R
import com.inces.incesclient.activities.ProductDetail
import com.inces.incesclient.helpers.ProductHelper
import com.inces.incesclient.models.Product
import com.inces.incesclient.util.Constants
import kotlinx.android.synthetic.main.product_card.view.*

class RelatedProductAdapter : RecyclerView.Adapter<RelatedProductAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.product_id == newItem.product_id
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.promoted_product_card, parent, false)
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.itemView.apply {
            productTitle.text = item.title
            productDescription.text = item.description
            productVendor.text = "Supplier - ${item.supplier_name}"
            val variantNumber = ProductHelper.getRandomNumber(item.variants.size)
            val imageNumber = ProductHelper.getRandomNumber(item.variants[variantNumber].image.size)
            if (!item.variants[variantNumber].image[imageNumber].img.contains("https")
            ) {
                Glide.with(context)
                    .load(Constants.IMAGE_URL + item.variants[variantNumber].image[imageNumber].img)
                    .into(productImage)
            } else {
                Glide.with(context)
                    .load(item.variants[variantNumber].image[imageNumber].img)
                    .into(productImage)
            }
            priceRange.text = ProductHelper.setPriceRange(item.variants)
            setOnClickListener {
                val i = Intent(context, ProductDetail::class.java)
                i.putExtra("Product", item)
                context.startActivity(i)
            }
        }
    }
}