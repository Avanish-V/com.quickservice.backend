package com.example.Authentication

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

class AuthenticationRepoImpl(db:MongoDatabase) : AuthenticationRepository {

    val userDataModelCollection = db.getCollection<UserDataModel>("Users")

    override suspend fun createUser(userDataModel: UserDataModel): Boolean {
        return userDataModelCollection.insertOne(userDataModel).wasAcknowledged()
    }

    override suspend fun getUser(userId:String): UserDataModel? {
        return userDataModelCollection.find(eq ("userId",userId)).firstOrNull()
    }

}