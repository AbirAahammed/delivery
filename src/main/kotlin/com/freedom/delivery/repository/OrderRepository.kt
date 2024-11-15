package com.freedom.delivery.repository

import com.freedom.delivery.model.Order
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : MongoRepository<Order, String> {
    fun findByName(name: String): List<Order>

}