package com.example.uas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.uas.databinding.ItemProductBinding
import com.example.uas.model.Product

class ProductAdapter(private var products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged() // Memberi tahu adapter bahwa data telah berubah
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.textViewProductName.text = product.title
            binding.textViewProductDescription.text = product.description
            binding.textViewProductPrice.text = "$${String.format("%.2f", product.price)}"
            binding.imageViewProduct.load(product.thumbnail) {
                crossfade(true)
                placeholder(R.mipmap.ic_launcher) // Placeholder jika gambar sedang dimuat
                error(R.drawable.ic_error) // Gambar jika ada error saat memuat
            }
        }
    }
}