package com.example.Model

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull


class MongoUserDataSource(db: MongoDatabase): UserDataSource {

    private val users = db.getCollection<User>("Admins")

    override suspend fun getUserByUsername(username: String): User? {
        return users.find(eq("userName", username)).firstOrNull()
    }


    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
}