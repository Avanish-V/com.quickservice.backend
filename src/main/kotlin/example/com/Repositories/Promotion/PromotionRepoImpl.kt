package com.example.Repositories.Promotion

import Network.Promotion.data.PromotionDataModel
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList

class PromotionRepoImpl(db:MongoDatabase) : PromotionRepository {

    val promotionCollection = db.getCollection<PromotionDataModel>("Promotion")

    override suspend fun createPromotion(promotionDataModel: PromotionDataModel): Boolean {

        return promotionCollection.insertOne(promotionDataModel).wasAcknowledged()
    }

    override suspend fun getPromotion(): List<PromotionDataModel> {
        return promotionCollection.find().toList()
    }
}