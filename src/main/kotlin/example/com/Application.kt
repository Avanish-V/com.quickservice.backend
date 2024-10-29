package example.com

import com.example.Authentication.AuthenticationRepoImpl
import com.example.Authentication.FirebaseAdmin
import com.example.Hashing.SHA256HashingService
import com.example.Model.MongoUserDataSource
import com.example.MongoClientConnection
import com.example.Repositories.Categories.ServiceCategoryImpl
import com.example.Repositories.Offer.OfferRepoImpl
import com.example.Repositories.Order.OrdersRepoImpl
import com.example.Repositories.PriceCalculation.PriceCalculationImpl
import com.example.Repositories.Professionals.ProfessionalRepoImpl
import com.example.Repositories.Promotion.PromotionRepoImpl
import com.example.Repositories.RateCard.RateCardRepoImpl
import com.example.Repositories.Reviews.ReviewRepoImpl
import com.example.Repositories.ServiceProducts.ServiceProduct
import com.example.Repositories.Users.UserRepoImpl
import com.example.Security.JwtTokenService
import com.example.Security.TokenConfig
import com.example.plugins.authConfigureRouting
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import example.com.CustomOTPAuth.OtpAuthImplementation
import example.com.CustomOTPAuth.OtpAuthRepository
import io.ktor.client.*
import io.ktor.server.application.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val tokenService = JwtTokenService()

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    val httpClient = HttpClient()

    val hashingService = SHA256HashingService()

    FirebaseAdmin.init()

    val database = MongoClientConnection.connectDB()
    val userDataSource = MongoUserDataSource(database)

    val auth = AuthenticationRepoImpl(database)
    val serviceCategoryImpl = ServiceCategoryImpl(database)
    val serviceProduct = ServiceProduct(database, userAuthenticationRepository = auth )
    val ordersRepoImpl = OrdersRepoImpl(database)
    val promotion = PromotionRepoImpl(database)
    val professional = ProfessionalRepoImpl(database)
    val user = UserRepoImpl(database)
    val review = ReviewRepoImpl(database)

    val offer = OfferRepoImpl(database, authenticationRepository = auth)
    val priceCalculation = PriceCalculationImpl(database,auth)
    val rateCard = RateCardRepoImpl(database)
    val otpAuthRepository = OtpAuthImplementation(database)

    configureSerialization()

    configureRouting(
        categoryRepository =  serviceCategoryImpl,
        serviceProductRepository =  serviceProduct,
        orderRepository =  ordersRepoImpl,
        offerRepository =  offer,
        promotionRepository =  promotion,
        professionalInterface =  professional,
        userRepository =  user,
        reviewRepository =  review,
        authenticationRepository =  auth,
        priceCalculationRepository =  priceCalculation,
        rateCardRepository =  rateCard,
        otpAuthRepository = otpAuthRepository
    )

    configureSecurity(tokenConfig)

    authConfigureRouting(userDataSource,hashingService,tokenService,tokenConfig)
}