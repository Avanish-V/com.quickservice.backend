package com.example.Repositories.RateCard

import Network.Rate.RateCardDataModel

interface RateCardRepository {

    suspend fun createRateCard(rateCardDataModel: RateCardDataModel) : Boolean

    suspend fun getRateCardList(applianceCategory:String) : List<RateCardDataModel>

}