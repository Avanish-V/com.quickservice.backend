package com.example.Repositories.RateCard

import Network.Rate.RateCardDataModel
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.Document

class RateCardRepoImpl(db:MongoDatabase) : RateCardRepository {

    private val rateCardCollection = db.getCollection<RateCardDataModel>("Rate Card")

    override suspend fun createRateCard(rateCardDataModel: RateCardDataModel): Boolean {
        return rateCardCollection.insertOne(rateCardDataModel).wasAcknowledged()
    }

    override suspend fun getRateCardList(applianceCategory : String): List<RateCardDataModel> {
        val filter = Document("applianceCategory",applianceCategory)
        return rateCardCollection.find(filter).toList()
    }

}