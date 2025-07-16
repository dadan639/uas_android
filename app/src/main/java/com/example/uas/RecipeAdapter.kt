package com.example.uas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.uas.databinding.ItemRecipeBinding
import com.example.uas.model.Recipe

class RecipeAdapter(private var recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged() // Memberi tahu adapter bahwa data telah berubah
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipes.size

    class RecipeViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.textViewRecipeName.text = recipe.name
            binding.textViewRecipeCuisine.text = "Cuisine: ${recipe.cuisine}"
            binding.textViewRecipeDifficulty.text = "Difficulty: ${recipe.difficulty}"
            binding.imageViewRecipe.load(recipe.image) {
                crossfade(true)
                placeholder(R.mipmap.ic_launcher) // Placeholder jika gambar sedang dimuat
                error(R.drawable.ic_error) // Gambar jika ada error saat memuat
            }
        }
    }
}