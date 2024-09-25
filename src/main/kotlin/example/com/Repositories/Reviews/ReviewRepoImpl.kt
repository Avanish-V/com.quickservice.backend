package com.example.Repositories.Reviews

import example.com.Repositories.Reviews.ReviewDataModel
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.Document

class ReviewRepoImpl(db:MongoDatabase) : ReviewRepository {

    val reviewCollection = db.getCollection<ReviewDataModel>("Reviews")

    override suspend fun createReview(reviewDataModel: ReviewDataModel): Boolean {
        return reviewCollection.insertOne(reviewDataModel).wasAcknowledged()
    }

    override suspend fun getAllReviews(): List<ReviewDataModel> {
       return reviewCollection.find().toList()
    }

    override suspend fun getReviewById(id: String, fieldName: String): Any {
        val filter = Document(fieldName,id)
        return reviewCollection.find(filter).toList()
    }

    override suspend fun getRatingById(id: String, fieldName: String): List<Int> {

        val filter = Document(fieldName,id)
        val filteredData = reviewCollection.find(filter).toList()
        return filteredData.map { it.rating }

    }
}