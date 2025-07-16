package com.example.uas.network

import com.example.uas.model.LoginRequest
import com.example.uas.model.LoginResponse
import com.example.uas.model.ProductsResponse
import com.example.uas.model.RecipesResponse
import retrofit2.Response // Penting: import Response dari retrofit2
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("products")
    suspend fun getAllProducts(): Response<ProductsResponse>

    @GET("recipes")
    suspend fun getAllRecipes(): Response<RecipesResponse>
}