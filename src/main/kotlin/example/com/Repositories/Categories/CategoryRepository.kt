package com.example.Repositories.Categories

import com.example.Model.CategoryModel

interface CategoryRepository  {

    suspend fun insertCategory(categoryModel: CategoryModel):Boolean
    suspend fun getCategory():List<CategoryModel>

}