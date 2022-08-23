package com.example.routings.seller_category

import com.example.repository.SellerCategoryRepository
import com.example.utils.Routes
import com.example.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getSellerCategories(
    sellerCategoryRepository: SellerCategoryRepository
) {
    route(Routes.SELLER_CATEGORY_ROUTE) {
        get("/") {

            val params = call.request.rawQueryParameters
            val id = params["id"]?.toInt()
            val title = params["title"]

            if (id == null && title == null) {
                sellerCategoryRepository.getSellersCategories().let { scResponse ->
                    when(scResponse) {
                        is ServiceResult.Success -> {
                            call.respond(
                                status = HttpStatusCode.OK,
                                message = scResponse.data
                            )
                        }
                        is ServiceResult.Error -> {
                            println("Error! Seller Category not found")
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = scResponse.errorCode
                            )
                        }
                    }
                }
            }

            id?.let {
                sellerCategoryRepository.getSellersCategoryById(it).let { scResponse ->
                    when(scResponse) {
                        is ServiceResult.Success -> {
                            call.respond(
                                status = HttpStatusCode.OK,
                                message = scResponse.data
                            )
                        }
                        is ServiceResult.Error -> {
                            println("Error! City not found")
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = scResponse.errorCode
                            )
                        }
                    }
                }
            }

            title?.let {
                sellerCategoryRepository.getSellerCategoriesByTitle(it).let { scResponse ->
                    when(scResponse) {
                        is ServiceResult.Success -> {
                            call.respond(
                                status = HttpStatusCode.OK,
                                message = scResponse.data
                            )
                        }
                        is ServiceResult.Error -> {
                            println("Error! City not found")
                            call.respond(
                                status = HttpStatusCode.BadRequest,
                                message = scResponse.errorCode
                            )
                        }
                    }
                }
            }
        }
    }
}