package com.example.uas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas.databinding.FragmentProductsBinding
import com.example.uas.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchProducts()

        binding.textViewErrorProducts.setOnClickListener {
            fetchProducts() // Coba lagi jika ada error
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList()) // Inisialisasi dengan list kosong
        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
        }
    }

    private fun fetchProducts() {
        binding.progressBarProducts.visibility = View.VISIBLE
        binding.textViewErrorProducts.visibility = View.GONE
        binding.recyclerViewProducts.visibility = View.GONE // Sembunyikan RecyclerView saat loading

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getAllProducts()
                if (response.isSuccessful) {
                    val productsResponse = response.body()
                    productsResponse?.products?.let {
                        productAdapter.updateProducts(it)
                        binding.recyclerViewProducts.visibility = View.VISIBLE
                    } ?: run {
                        binding.textViewErrorProducts.text = "No products found."
                        binding.textViewErrorProducts.visibility = View.VISIBLE
                    }
                } else {
                    binding.textViewErrorProducts.text = "Failed to load products: ${response.message()}\nTap to retry."
                    binding.textViewErrorProducts.visibility = View.VISIBLE
                    Toast.makeText(context, "Failed to load products: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.textViewErrorProducts.text = "Error fetching products: ${e.message}\nTap to retry."
                binding.textViewErrorProducts.visibility = View.VISIBLE
                Toast.makeText(context, "Error fetching products: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } finally {
                binding.progressBarProducts.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}