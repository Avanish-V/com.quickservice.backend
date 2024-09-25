package com.example.Repositories.Categories

import example.com.Repositories.Categories.CategoryModel

interface CategoryRepository  {

    suspend fun insertCategory(categoryModel: CategoryModel):Boolean
    suspend fun getCategory():List<CategoryModel>

}