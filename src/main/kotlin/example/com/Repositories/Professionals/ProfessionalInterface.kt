package com.example.Repositories.Professionals

import com.example.Model.AccountStatus
import com.example.Model.ProfessionalById
import com.example.Model.ProfessionalDataModel

interface ProfessionalInterface {

    suspend fun createProfessionalProfile(professionalDataModel: ProfessionalDataModel):Boolean

    suspend fun getProfessionalsForAdmin():List<ProfessionalDataModel>

    suspend fun updateAccountStatus(id:String,accountStatus: AccountStatus) : Boolean

    suspend fun getProfessionalById(id:String):ProfessionalById


}