package com.example.Repositories.Categories

import com.example.Model.CategoryModel
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList


class ServiceCategory (db:MongoDatabase): CategoryRepository {

    private val  collection = db.getCollection<CategoryModel>("Categories")

    override suspend fun insertCategory(categoryModel: CategoryModel):Boolean{
        return try {
            collection.insertOne(categoryModel).wasAcknowledged()
        } catch (e: Exception) {
            println("Error inserting category: ${e.message}")
            false
        }
    }

    override suspend fun getCategory():List<CategoryModel>{
        return try {
            collection.find().toList()
        } catch (e: Exception) {
            println("Error fetching categories: ${e.message}")
            emptyList()
        }
    }

}