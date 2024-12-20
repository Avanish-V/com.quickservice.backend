package com.example.Repositories.ServiceProducts

import com.example.Model.ServiceProductsModel

interface ServiceProductRepository {

    suspend fun insertProduct(serviceProductsModel: ServiceProductsModel):Boolean

    suspend fun getServiceProduct(id:String,UUID:String):List<ServiceProductsModel>

    suspend fun getServiceProductForAdmin(id:String):List<ServiceProductsModel>

    suspend fun deleteProduct(id:String):Boolean


}