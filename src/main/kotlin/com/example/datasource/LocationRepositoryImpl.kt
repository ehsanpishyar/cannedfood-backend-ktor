package com.example.datasource

import com.example.models.Location
import com.example.models.responses.LocationResponse
import com.example.repository.LocationRepository
import com.example.tables.CityTable
import com.example.tables.DatabaseFactory.dbQuery
import com.example.tables.LocationTable
import com.example.tables.StateTable
import com.example.utils.ErrorCode
import com.example.utils.ServiceResult
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

class LocationRepositoryImpl: LocationRepository {

    override suspend fun insertLocation(location: Location): ServiceResult<Location> {
        return try {
            dbQuery {
                LocationTable.insert {
                    it[title] = location.title
                    it[lat] = location.lat
                    it[lon] = location.lon
                    it[cityId] = location.city_id
                }
                    .resultedValues?.singleOrNull()?.let {
                        ServiceResult.Success(rowToLocation(it)!!)
                    } ?: ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        } catch (e: Exception) {
            when (e) {
                is ExposedSQLException -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun getLocations(cityId: Int): ServiceResult<List<LocationResponse?>> {
        return try {
            dbQuery {
                (LocationTable innerJoin CityTable innerJoin StateTable)
                    .select {
                        LocationTable.cityId eq cityId
                    }
                    .orderBy(LocationTable.id to SortOrder.ASC)
                    .map { rowToLocationResponse(it)!! }
            }.let {
                ServiceResult.Success(it)
            }
        } catch (e: Exception) {
            println(e)
            when (e) {
                is ExposedSQLException -> {
                    println("An Error occurred due to response user by username")
                    ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun getLocationById(locationId: Long): ServiceResult<LocationResponse?> {
        return try {
            dbQuery {
                (LocationTable innerJoin CityTable innerJoin StateTable)
                    .select { LocationTable.id eq locationId }
                        .map { rowToLocationResponse(it)!! }
                        .singleOrNull()
            }.let {
                ServiceResult.Success(it)
            }
        } catch (e: Exception) {
            println(e)
            when (e) {
                is ExposedSQLException -> {
                    println("An Error occurred due to response user by username")
                    ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun getLocationsByTitle(locationTitle: String?): ServiceResult<List<LocationResponse?>> {
        return try {
            dbQuery {
                (LocationTable innerJoin CityTable innerJoin StateTable)
                    .select { LocationTable.title like  "$locationTitle%" }
                    .map { rowToLocationResponse(it)!! }
            }.let {
                ServiceResult.Success(it)
            }
        } catch (e: Exception) {
            println(e)
            when (e) {
                is ExposedSQLException -> {
                    println("An Error occurred due to response user by username")
                    ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                }
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun updateLocation(locationId: Long, location: Location): ServiceResult<Location> {
        return try {
            val id = dbQuery {
                LocationTable.update({
                    LocationTable.id eq locationId
                }) {
                    it[id] = location.id
                    it[title] = location.title
                    it[lat] = location.lat
                    it[lon] = location.lon
                    it[cityId] = location.city_id
                }
            }
            dbQuery {
                LocationTable.select {
                    LocationTable.id eq id.toLong()
                }.map { rowToLocation(it) }
                    .singleOrNull()
            }.let {
                ServiceResult.Success(it!!)
            }
        } catch (e: Exception) {
            when (e) {
                is ExposedSQLException -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
                else -> ServiceResult.Error(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun deleteLocation(locationId: Long) {
        dbQuery {
            LocationTable.deleteWhere {
                LocationTable.id eq locationId
            }
        }
    }

    override suspend fun deleteLocations() {
        dbQuery {
            LocationTable.deleteAll()
        }
    }

    private fun rowToLocation(row: ResultRow?): Location? {
        if (row == null) return null

        return Location(
            id = row[LocationTable.id],
            title = row[LocationTable.title],
            lat = row[LocationTable.lat],
            lon = row[LocationTable.lon],
            city_id = row[LocationTable.cityId]
        )
    }

    private fun rowToLocationResponse(row: ResultRow?): LocationResponse? {
        if (row == null) return null

        return LocationResponse(
            title = row[LocationTable.title],
            lat = row[LocationTable.lat],
            lon = row[LocationTable.lon],
            city = row[CityTable.title],
            state = row[StateTable.title]
        )
    }
}