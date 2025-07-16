package com.example.uas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas.databinding.FragmentRecipesBinding
import com.example.uas.network.RetrofitClient
import kotlinx.coroutines.launch

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchRecipes()

        binding.textViewErrorRecipes.setOnClickListener {
            fetchRecipes() // Coba lagi jika ada error
        }
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(emptyList()) // Inisialisasi dengan list kosong
        binding.recyclerViewRecipes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipeAdapter
        }
    }

    private fun fetchRecipes() {
        binding.progressBarRecipes.visibility = View.VISIBLE
        binding.textViewErrorRecipes.visibility = View.GONE
        binding.recyclerViewRecipes.visibility = View.GONE // Sembunyikan RecyclerView saat loading

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getAllRecipes()
                if (response.isSuccessful) {
                    val recipesResponse = response.body()
                    recipesResponse?.recipes?.let {
                        recipeAdapter.updateRecipes(it)
                        binding.recyclerViewRecipes.visibility = View.VISIBLE
                    } ?: run {
                        binding.textViewErrorRecipes.text = "No recipes found."
                        binding.textViewErrorRecipes.visibility = View.VISIBLE
                    }
                } else {
                    binding.textViewErrorRecipes.text = "Failed to load recipes: ${response.message()}\nTap to retry."
                    binding.textViewErrorRecipes.visibility = View.VISIBLE
                    Toast.makeText(context, "Failed to load recipes: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.textViewErrorRecipes.text = "Error fetching recipes: ${e.message}\nTap to retry."
                binding.textViewErrorRecipes.visibility = View.VISIBLE
                Toast.makeText(context, "Error fetching recipes: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } finally {
                binding.progressBarRecipes.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}