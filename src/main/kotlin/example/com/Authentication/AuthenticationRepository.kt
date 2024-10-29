package com.example.Authentication

interface AuthenticationRepository {

    suspend fun createUser(userDataModel: UserDataModel) : Boolean

    suspend fun getUser(userId:String) : UserDataModel?

}
