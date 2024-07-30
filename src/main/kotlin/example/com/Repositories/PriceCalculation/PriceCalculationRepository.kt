package com.example.Repositories.PriceCalculation

import com.byteapps.serrvicewala.Features.Orders.data.PriceDetails
import kotlinx.coroutines.flow.Flow

interface PriceCalculationRepository {

    suspend fun setPriceWithCoupon(userId:String,productId: String,couponCode:String?= null,quantity:Int): PriceDetails
}