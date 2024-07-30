package com.example.Repositories.Users

import com.example.Authentication.UserDataModel
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.bson.Document

class UserRepoImpl(db:MongoDatabase) : UserRepository {

    val userCollection = db.getCollection<UserDataModel>("Users")

    override suspend fun createUser(userDataModel: UserDataModel): Boolean {
        return withContext(Dispatchers.IO){
            userCollection.insertOne(userDataModel).wasAcknowledged()
        }
    }

    override suspend fun getUserById(userUID: String): UserDataModel? {
       val filter = Document("UUID",userUID)
        return userCollection.find(filter).firstOrNull()
    }

    override suspend fun getAllUsers(): List<UserDataModel> {
        return userCollection.find().toList()
    }


}