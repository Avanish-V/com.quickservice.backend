package com.example.Repositories.Reviews

import com.example.Model.ReviewDataModel

interface ReviewRepository {

    suspend fun createReview(reviewDataModel: ReviewDataModel) : Boolean

    suspend fun getAllReviews():List<ReviewDataModel>

    suspend fun getReviewById(id:String,fieldName:String) : Any

    suspend fun getRatingById(id: String,fieldName:String) : List<Int>



}