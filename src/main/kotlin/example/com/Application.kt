package example.com

import com.example.Authentication.AuthenticationRepoImpl
import com.example.Authentication.FirebaseAdmin
import com.example.MongoClientConnection
import com.example.Repositories.Categories.ServiceCategory
import com.example.Repositories.Offer.OfferRepoImpl
import com.example.Repositories.Order.Orders
import com.example.Repositories.PriceCalculation.PriceCalculationImpl
import com.example.Repositories.Professionals.ProfessionalRepoImpl
import com.example.Repositories.Promotion.PromotionRepoImpl
import com.example.Repositories.RateCard.RateCardRepoImpl
import com.example.Repositories.Reviews.ReviewRepoImpl
import com.example.Repositories.ServiceProducts.ServiceProduct
import com.example.Repositories.Users.UserRepoImpl
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import io.ktor.server.application.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {


    FirebaseAdmin.init()


    val database = MongoClientConnection.connectDB()


    val serviceCategory = ServiceCategory(database)
    val serviceProduct = ServiceProduct(database)
    val orders = Orders(database)
    val promotion = PromotionRepoImpl(database)
    val professional = ProfessionalRepoImpl(database)
    val user = UserRepoImpl(database)
    val review = ReviewRepoImpl(database)
    val auth = AuthenticationRepoImpl(database)
    val offer = OfferRepoImpl(database, authenticationRepository = auth)
    val priceCalculation = PriceCalculationImpl(database,auth)
    val rateCard = RateCardRepoImpl(database)


    configureSerialization()

    //configureFirebaseAuth()

    configureRouting(
        categoryRepository =  serviceCategory,
        serviceProductRepository =  serviceProduct,
        orderRepository =  orders,
        offerRepository =  offer,
        promotionRepository =  promotion,
        professionalInterface =  professional,
        userRepository =  user,
        reviewRepository =  review,
        authenticationRepository =  auth,
        priceCalculationRepository =  priceCalculation,
        rateCardRepository =  rateCard
    )
}