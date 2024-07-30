package com.example.Repositories.Professionals

import com.example.Model.AccountStatus
import com.example.Model.ProfessionalById
import com.example.Model.ProfessionalDataModel
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.conversions.Bson

class ProfessionalRepoImpl(db:MongoDatabase) : ProfessionalInterface {

    val professionalCollection = db.getCollection<ProfessionalDataModel>("Professionals")

    override suspend fun createProfessionalProfile(professionalDataModel: ProfessionalDataModel): Boolean {
        return professionalCollection.insertOne(professionalDataModel).wasAcknowledged()
    }

    override suspend fun getProfessionalsForAdmin(): List<ProfessionalDataModel> {
        return professionalCollection.find().toList()
    }

    override suspend fun updateAccountStatus(id:String,accountStatus: AccountStatus): Boolean {
        val filter = Document("professionalId", id)
        val data = Document("\$set", Document("accountStatus", accountStatus))

        return professionalCollection.updateOne(filter,data).wasAcknowledged()
    }

    override suspend fun getProfessionalById(id: String): ProfessionalById {
        return withContext(Dispatchers.IO){

            try {
                val filter: Bson = eq("professionalId", id)
                val document = professionalCollection.find(filter).firstOrNull()
                document?.let {
                    ProfessionalById(
                        professionalId = it.professionalId,
                        professionalName = it.professionalName,
                        photoUrl = it.photoUrl
                    )
                }
            } catch (e: Exception) {
                println("Error fetching professional by ID: ${e.localizedMessage}")
                null
            }!!

        }

    }


}