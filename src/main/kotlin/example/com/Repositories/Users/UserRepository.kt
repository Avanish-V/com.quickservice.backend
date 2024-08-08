package com.example.Repositories.Users

import com.example.Authentication.UserDataModel

interface UserRepository  {

    suspend fun createUser(userDataModel: UserDataModel) : Boolean

    suspend fun getUserById(userUID:String) : UserDataModel?

    suspend fun getAllUsers():List<UserDataModel>

    suspend fun updateUser(
      userDataModel: UserDataModel
    ):Boolean

}