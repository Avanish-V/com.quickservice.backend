package com.example.Repositories.Promotion

import example.com.Repositories.Promotion.PromotionDataModel

interface PromotionRepository {

    suspend fun createPromotion(promotionDataModel: PromotionDataModel):Boolean

    suspend fun getPromotion():List<PromotionDataModel>

}