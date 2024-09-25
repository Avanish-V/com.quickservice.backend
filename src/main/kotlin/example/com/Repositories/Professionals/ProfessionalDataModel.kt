package com.example.Model

data class ProfessionalDataModel(

    val professionalId : String,
    val professionalName : String,
    val photoUrl : String,
    val mobile : String,
    val email : String,
    val adharNumber : String,
    val address : String,
    val profession : String,
    val accountStatus: AccountStatus

)
data class AccountStatus(
    val active : Boolean,
    val status : String

)

enum class Status{
    Pending,
    Active,
    Terminated
}

data class ProfessionalById(

    val professionalId : String,
    val professionalName : String,
    val photoUrl : String,

)