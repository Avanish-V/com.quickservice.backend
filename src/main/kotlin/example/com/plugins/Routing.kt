package com.example.plugins

import com.example.Authentication.AuthenticationRepository
import com.example.Authentication.authRoute
import com.example.Hashing.HashingService
import com.example.Model.UserDataSource
import com.example.Repositories.Categories.CategoryRepository
import com.example.Repositories.Offer.OfferRepository
import com.example.Repositories.Order.OrderRepository
import com.example.Repositories.PriceCalculation.PriceCalculationRepository
import com.example.Repositories.Professionals.ProfessionalInterface
import com.example.Repositories.Promotion.PromotionRepository
import com.example.Repositories.RateCard.RateCardRepository
import com.example.Repositories.Reviews.ReviewRepository
import com.example.Repositories.ServiceProducts.ServiceProductRepository
import com.example.Repositories.Users.UserRepository
import com.example.Routes.*
import com.example.Security.JwtTokenService
import com.example.Security.TokenConfig
import com.plcoding.authenticate
import com.plcoding.getSecretInfo
import com.plcoding.signIn
import com.plcoding.signUp
import example.com.CustomOTPAuth.OtpAuthRepository
import example.com.Routes.otpAuthRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import professionalRoute
import userRoute

fun Application.configureRouting(
    categoryRepository: CategoryRepository,
    serviceProductRepository: ServiceProductRepository,
    orderRepository: OrderRepository,
    offerRepository: OfferRepository,
    promotionRepository: PromotionRepository,
    professionalInterface: ProfessionalInterface,
    userRepository: UserRepository,
    reviewRepository: ReviewRepository,
    authenticationRepository: AuthenticationRepository,
    priceCalculationRepository: PriceCalculationRepository,
    rateCardRepository: RateCardRepository,
    otpAuthRepository: OtpAuthRepository
) {


    routing {
        get("/name") {
            call.respond(HttpStatusCode.OK, "name")
        }
    }


    routing {

        categoryRoute(categoryRepository)
        serviceProductRoute(serviceProductRepository)
        ordersRoute(orderRepository)
        offerRoute(offerRepository)
        promotionRoute(promotionRepository)
        professionalRoute(professionalInterface)
        userRoute(userRepository)
        reviewRoute(reviewRepository)
        authRoute(authenticationRepository)
        priceCalculationRoute(priceCalculationRepository)
        rateCardRoute(rateCardRepository)
        otpAuthRoute(otpAuthRepository)

    }

}

fun Application.authConfigureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: JwtTokenService,
    config: TokenConfig
) {
    routing {

        signIn(userDataSource,hashingService,tokenService,config)
        signUp(hashingService,userDataSource)
        authenticate()
        getSecretInfo()


    }
}