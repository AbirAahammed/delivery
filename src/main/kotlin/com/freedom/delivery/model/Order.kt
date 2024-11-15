package com.freedom.delivery.model

import kotlinx.serialization.Serializable
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.annotation.Id


@Serializable
@Document(collection = "order")
data class Order(
    @Id
    var id: String?=null,
    val name: String,
    val price: Double,
    val status : Status,
    val sessionId: String)