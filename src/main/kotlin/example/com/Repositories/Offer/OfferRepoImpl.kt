package com.example.Repositories.Offer

import example.com.Repositories.Offer.OfferDataModel
import com.example.Authentication.AuthenticationRepository
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.set
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.Document

class OfferRepoImpl(db:MongoDatabase,private val authenticationRepository: AuthenticationRepository) : OfferRepository {

    val collection = db.getCollection<OfferDataModel>("Offer")

    override suspend fun createOffer(offerDataModel: OfferDataModel): Boolean {
       return collection.insertOne(offerDataModel).wasAcknowledged()
    }

    override suspend fun getOffer(userId: String): List<OfferDataModel> {

        val filter = Document("status",true)

        val user = authenticationRepository.getUser(userId)
            ?: throw IllegalArgumentException("User not found")

        return collection.find(filter).toList().filter { offer ->
            !(!user.isFirstUser && offer.userType == "First User")
        }
    }

    override suspend fun getOfferCoupons(
        userId: String,
        productTAG: String,
        productId: String,
        workType: String
    ): List<OfferDataModel> {

        // Create the base filter document with mandatory fields

        val list = mutableListOf<OfferDataModel>()

        val filter1 = Document()
            .append("status",true) // Assuming status should be "active"
            .append("workType", workType)
            .append("appliesTo", "All Products")


        val filter2 = Document()
            .append("status",true) // Assuming status should be "active"
            .append("workType", workType)
            .append("productTAG", productTAG)
            .append("appliesTo", "Category")

        val filter3 = Document()
            .append("status",true) // Assuming status should be "active"
            .append("workType", workType)
            .append("productTAG", productTAG)
            .append("productId", productId)

        val filter4 = Document()
            .append("status",true) // Assuming status should be "active"
            .append("userType", "First User")




        val user = authenticationRepository.getUser(userId)
            ?: throw IllegalArgumentException("User not found")

       val applyFirstFilter = collection.find(filter1).toList()

        if (applyFirstFilter.isNotEmpty()){
            applyFirstFilter.map {
                list.add(it)
            }

        }
        val applySecondFilter = collection.find(filter2).toList()

        if (applySecondFilter.isNotEmpty()){
            applySecondFilter.map {
                list.add(it)
            }

        }

        val applyThirdFilter = collection.find(filter3).toList()

        if (applyThirdFilter.isNotEmpty()){
            applyThirdFilter.map {
                list.add(it)

            }

        }

        if (user.isFirstUser){

            val applyFourthFilter = collection.find(filter4).toList()

            if (applyFourthFilter.isNotEmpty()){
                applyFourthFilter.map {
                    list.add(it)
                }

            }
        }

        return list

    }

    override suspend fun getOfferListForAdmin(): List<OfferDataModel> {
        return collection.find().toList()
    }


    override suspend fun updateStatus(status: Boolean,id:String): Boolean {
       return collection.updateOne(eq("promoCode",id),set("status",status)).wasAcknowledged()
    }

    override suspend fun deleteOffer(id: String): Boolean {
        return collection.deleteOne(eq("promoCode",id)).wasAcknowledged()
    }


}