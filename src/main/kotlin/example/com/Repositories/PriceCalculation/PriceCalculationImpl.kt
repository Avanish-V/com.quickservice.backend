package com.example.Repositories.PriceCalculation

import example.com.Repositories.Offer.OfferDataModel
import com.byteapps.serrvicewala.Features.Orders.data.Coupon
import com.byteapps.serrvicewala.Features.Orders.data.PriceDetails
import com.example.Authentication.AuthenticationRepository
import com.example.Model.ServiceProductsModel
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.Document

class PriceCalculationImpl(db:MongoDatabase,private val authenticationRepository: AuthenticationRepository) : PriceCalculationRepository {

    val productCollection = db.getCollection<ServiceProductsModel>("ServiceProducts")
    val offerCollection = db.getCollection<OfferDataModel>("Offer")

    override suspend fun setPriceWithCoupon(
        userId: String,
        productId: String,
        couponCode: String?,
        quantity: Int
    ): PriceDetails {
        val user = authenticationRepository.getUser(userId)
            ?: throw IllegalArgumentException("User not found")

        val productFilter = Document("productId", productId)
        val couponFilter = Document("promoCode", couponCode)

        val serviceProduct = productCollection.find(filter = productFilter).firstOrNull()
            ?: throw IllegalArgumentException("Product not found")

        val promoCodeData = offerCollection.find(filter = couponFilter).firstOrNull()

        val calculatedTax = serviceProduct.tax * quantity
        val baseTotal = serviceProduct.price * quantity + calculatedTax

        return if (promoCodeData == null || couponCode.isNullOrEmpty()) {
            PriceDetails(
                price = serviceProduct.price,
                tax = calculatedTax,
                quantity = quantity,
                total = baseTotal,
                coupon = null
            )
        } else {
            val discount = if (promoCodeData.discountType == "Percentage") {
                (serviceProduct.price * promoCodeData.discount.toInt()) / 100
            } else {
                promoCodeData.discount.toInt()
            }

            val total = baseTotal - discount

            PriceDetails(
                price = serviceProduct.price,
                tax = calculatedTax,
                quantity = quantity,
                total = total,
                coupon = Coupon(
                    offerTitle = "",
                    couponCode = promoCodeData.promoCode,
                    discount = discount
                )
            )
        }
    }

}