package com.example.Repositories.Promotion

import Network.Promotion.data.PromotionDataModel

interface PromotionRepository {

    suspend fun createPromotion(promotionDataModel: PromotionDataModel):Boolean

    suspend fun getPromotion():List<PromotionDataModel>

}