package com.example.Repositories.ServiceProducts

import com.example.Model.Rating
import example.com.Repositories.Reviews.ReviewDataModel
import com.example.Model.ServiceProductsModel
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.Document

class ServiceProduct (db:MongoDatabase): ServiceProductRepository {

    val productCollection = db.getCollection<ServiceProductsModel>("ServiceProducts")
    val reviewCollection = db.getCollection<ReviewDataModel>("Reviews")

    override suspend fun insertProduct(serviceProductsModel: ServiceProductsModel): Boolean {

        return productCollection.insertOne(serviceProductsModel).wasAcknowledged()

    }

    override suspend fun getServiceProduct(id:String): List<ServiceProductsModel> {

        val allProducts = productCollection.find(eq("productTAG",id)).toList()

        return allProducts.map { product ->

            val filter = Document("serviceProductId",product.productId)
            val productRatings = reviewCollection.find(filter).toList()

            val averageRating = if (productRatings.isNotEmpty()) {
                productRatings.map { it.rating }.average()
            } else {
                4.8
            }
            val ratingCount = if (productRatings.isNotEmpty()) {
                productRatings.map { it.rating }.count()
            } else {
               4
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

                )
            )
        }
    }

    override suspend fun deleteProduct(id: String): Boolean {
        return productCollection.deleteOne(eq("serviceId",id)).wasAcknowledged()
    }
}