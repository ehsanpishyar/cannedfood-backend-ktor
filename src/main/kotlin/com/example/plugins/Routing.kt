package com.example.plugins

import com.example.datasource.*
import com.example.repository.*
import com.example.routings.city.deleteCities
import com.example.routings.city.getCities
import com.example.routings.city.insertNewCity
import com.example.routings.city.updateCity
import com.example.routings.food_category.deleteFoodCategories
import com.example.routings.food_category.getFoodCategories
import com.example.routings.food_category.insertNewFoodCategory
import com.example.routings.food_category.updateFoodCategory
import com.example.routings.location.deleteLocations
import com.example.routings.location.getLocations
import com.example.routings.location.insertNewLocation
import com.example.routings.location.updateLocation
import com.example.routings.result_category.deleteResultCategories
import com.example.routings.result_category.getResultCategories
import com.example.routings.result_category.insertNewResultCategory
import com.example.routings.result_category.updateResultCategory
import com.example.routings.seller_category.deleteSellerCategories
import com.example.routings.seller_category.getSellerCategories
import com.example.routings.state.getStates
import com.example.routings.user.deleteUsers
import com.example.routings.user.getUsers
import com.example.routings.user.insertNewUser
import com.example.routings.user.updateUser
import com.example.usecases.InsertUserUseCase
import com.example.utils.ValidateUserEmail
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    routing {
        route("/") {
            get {
                call.respondText("Hello")
            }

            // User Section
            val userRepository: UserRepository = UserRepositoryImpl()
            val emailValidation = ValidateUserEmail()
            val insertUserUseCase = InsertUserUseCase(emailValidation, userRepository)
            insertNewUser(insertUserUseCase)
            getUsers(userRepository)
            updateUser(userRepository)
            deleteUsers(userRepository)

            // State Section
            val stateRepository: StateRepository = StateRepositoryImpl()
            getStates(stateRepository)

            // City Section
            val cityRepository: CityRepository = CityRepositoryImpl()
            insertNewCity(cityRepository)
            getCities(cityRepository)
            updateCity(cityRepository)
            deleteCities(cityRepository)

            // Location Section
            val locationRepository: LocationRepository = LocationRepositoryImpl()
            insertNewLocation(locationRepository)
            getLocations(locationRepository)
            updateLocation(locationRepository)
            deleteLocations(locationRepository)

            // Seller Category Section
            val sellerCategoryRepository: SellerCategoryRepository = SellerCategoryRepositoryImpl()
            getSellerCategories(sellerCategoryRepository)
            deleteSellerCategories(sellerCategoryRepository)

            // Results Category Section
            val resultCategoryRepository: ResultCategoryRepository = ResultCategoryRepositoryImpl()
            insertNewResultCategory(resultCategoryRepository)
            getResultCategories(resultCategoryRepository)
            updateResultCategory(resultCategoryRepository)
            deleteResultCategories(resultCategoryRepository)

            // Food Category Section
            val foodCategoryRepository: FoodCategoryRepository = FoodCategoryRepositoryImpl()
            insertNewFoodCategory(foodCategoryRepository)
            getFoodCategories(foodCategoryRepository)
            updateFoodCategory(foodCategoryRepository)
            deleteFoodCategories(foodCategoryRepository)

        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
