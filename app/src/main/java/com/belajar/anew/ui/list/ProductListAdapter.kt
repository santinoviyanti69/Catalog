package com.belajar.MyCatalog.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.belajar.MyCatalog.R
import com.belajar.MyCatalog.data.model.Product
import com.belajar.MyCatalog.databinding.ItemProductBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ProductListAdapter(private val listener: ProductListAdapterListener): PagingDataAdapter<Product, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    //untuk membuat objek ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder.onCreate(parent)
    }

    //untuk menghubungkan data yang ada dengan objek ViewHolder.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as ViewHolder).onBind(product, listener)
    }

    class ViewHolder private constructor(private val binding: ItemProductBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(product: Product?, listener: ProductListAdapterListener) {

            product?.let {
                Glide.with(itemView.context).load(it.images[0])
                    .placeholder(R.drawable.baseline_downloading_24)
                    .transform(CenterInside(), RoundedCorners(24))
                    .into(binding.productImage)
                binding.productName.text = it.title
                binding.productPrice.text = it.formattedPrice
                binding.ratingBar.rating = it.rating.toFloat()
                binding.root.setOnClickListener{
                    listener.onClickProduct(product)
                }



            }
        }

        // object dalam class
        companion object{
            fun onCreate(parent: ViewGroup): ViewHolder {
                val itemView =
                    ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(itemView)
            }
        }
    }

    // object luar class
    companion object {
        // compare data lama vs data baru
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {

            // apakah itemnya sama
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            // apakah konten itemnya sama secara keseluruhan
            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }

    }
}

interface ProductListAdapterListener {
    fun onClickProduct(product: Product)
}


