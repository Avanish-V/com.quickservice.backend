package example.com.Repositories.Reviews

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDataModel(

    @SerialName("reviewText")val reviewText : String,
    @SerialName("reviewBy")val reviewBy : String,
    @SerialName("rating")val rating : Int,
    @SerialName("date")val date : String,
    @SerialName("serviceProductId")val serviceProductId : String,
    @SerialName("professionalId")val professionalId:String,
    @SerialName("orderId")val orderId : String

)



