package com.example.Repositories.Offer

import example.com.Repositories.Offer.OfferDataModel

interface OfferRepository {

    suspend fun createOffer(offerDataModel: OfferDataModel):Boolean

    suspend fun getOffer(userId:String):List<OfferDataModel>

    suspend fun getOfferCoupons(
        userId: String,
        productTAG:String,
        productId:String,
        workType:String
    ):List<OfferDataModel>

    suspend fun getOfferListForAdmin():List<OfferDataModel>

    suspend fun updateStatus(status:Boolean,id:String):Boolean

    suspend fun deleteOffer(id: String) : Boolean

}