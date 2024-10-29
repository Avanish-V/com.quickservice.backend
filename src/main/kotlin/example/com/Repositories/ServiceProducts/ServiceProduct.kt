package com.example.Repositories.ServiceProducts

import com.example.Authentication.AuthenticationRepository
import com.example.Model.Rating
import example.com.Repositories.Reviews.ReviewDataModel
import com.example.Model.ServiceProductsModel
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import example.com.Repositories.Offer.OfferDataModel
import kotlinx.coroutines.flow.toList
import org.bson.Document

class ServiceProduct (db:MongoDatabase,private val userAuthenticationRepository: AuthenticationRepository): ServiceProductRepository {

    val productCollection = db.getCollection<ServiceProductsModel>("ServiceProducts")
    val reviewCollection = db.getCollection<ReviewDataModel>("Reviews")
    val offerCollection = db.getCollection<OfferDataModel>("Offer")

    override suspend fun insertProduct(serviceProductsModel: ServiceProductsModel): Boolean {

        return productCollection.insertOne(serviceProductsModel).wasAcknowledged()

    }

    override suspend fun getServiceProduct(id:String,UUID:String): List<ServiceProductsModel> {

        val allProducts = productCollection.find(eq("productTAG",id)).toList()

        return allProducts.map { product ->

            val filter = Document("serviceProductId",product.productId)
            val productRatings = reviewCollection.find(filter).toList()

            val averageRating = if (productRatings.isNotEmpty()) {
                productRatings.map { it.rating }.average()
            } else {
                0.0
            }
            val ratingCount = if (productRatings.isNotEmpty()) {
                productRatings.map { it.rating }.count()
            } else {
               0
            }

            val list = mutableListOf<OfferDataModel>()

            val user = userAuthenticationRepository.getUser(UUID)
                ?: throw IllegalArgumentException("User not found")

            val filter1 = Document()
                .append("status",true) // Assuming status should be "active"
                .append("workType", "All")
                .append("productTAG", product.productTAG)
                .append("appliesTo", "Category")
                .append("userType", "All Users")


            val filter2 = Document()
                .append("status",true) // Assuming status should be "active"
                .append("workType", product.workType)
                .append("productTAG", product.productTAG)
                .append("appliesTo", "Category")
                .append("userType", "All Users")

            val filter3 = Document()
                .append("status",true) // Assuming status should be "active"
                .append("workType", product.workType)
                .append("productTAG", product.productTAG)
                .append("productId", product.productId)
//
            val filter4 = Document()
                .append("status",true) // Assuming status should be "active"
                .append("appliesTo", "All Products")
                .append("userType", "First User")

            val filter5 = Document()
                .append("status",true) // Assuming status should be "active"
                .append("appliesTo", "All Products")
                .append("userType", "All Users")
                .append("workType", "All")


            val applyFirstFilter = offerCollection.find(filter1).toList()

            if (applyFirstFilter.isNotEmpty()){
                applyFirstFilter.map {
                    list.add(it)
                }

            }
            val applySecondFilter = offerCollection.find(filter2).toList()

            if (applySecondFilter.isNotEmpty()){
                applySecondFilter.map {
                    list.add(it)
                }

            }
//
            val applyThirdFilter = offerCollection.find(filter3).toList()

            if (applyThirdFilter.isNotEmpty()){
                applyThirdFilter.map {
                    list.add(it)

                }

            }

            val applyFifthFilter = offerCollection.find(filter5).toList()

            if (applyFifthFilter.isNotEmpty()){
                applyFifthFilter.map {
                    list.add(it)

                }

            }

            if (user.isFirstUser){

                val applyFourthFilter = offerCollection.find(filter4).toList()

                if (applyFourthFilter.isNotEmpty()){
                    applyFourthFilter.map {
                        list.add(it)
                    }

                }
            }



            ServiceProductsModel(
                productTitle = product.productTitle,
                productImage = product.productImage,
                productId = product.productId,
                productTAG = product.productTAG,
                workType = product.workType,
                price = product.price,
                tax = product.tax,
                description = product.description,
                rating = Rating(
                    rating = String.format("%.1f", averageRating),
                    count = ratingCount.toString(),

                ),
                offerAvailable = list.count()
            )
        }
    }

    override suspend fun deleteProduct(id: String): Boolean {
        return productCollection.deleteOne(eq("serviceId",id)).wasAcknowledged()
    }
}