package com.example


import org.bson.Document
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.runBlocking

object MongoClientConnection {

    fun connectDB(): MongoDatabase {



//        val user = System.getenv("MONGODB_USER") ?: throw IllegalStateException("Missing MONGODB_USER environment variable")
//        val password = System.getenv("DB_PASSWORD") ?: throw IllegalStateException("Missing MONGODB_PASSWORD environment variable")
//        val hostname = System.getenv("MONGODB_HOST") ?: throw IllegalStateException("Missing MONGODB_HOSTNAME environment variable")
//        val databaseName = System.getenv("MONGODB_DATABASE") ?: throw IllegalStateException("Missing MONGODB_DATABASE environment variable")
//        val port = System.getenv("PORT") ?:"8080"
//        val connectionString = "mongodb+srv://$user:$password@$hostname/?retryWrites=true&w=majority&appName=$databaseName"


        val connectionString = "mongodb+srv://ashutoshkumar1552002:avanish8787@quickservicecluster.mdlniyf.mongodb.net/?retryWrites=true&w=majority&appName=QuickServiceCluster"

        val mongoClient = MongoClient.create(connectionString)

        return mongoClient.getDatabase("QuickServiceCluster")
    }
}

