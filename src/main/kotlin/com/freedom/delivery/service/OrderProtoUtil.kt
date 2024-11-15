package com.freedom.delivery.service


import com.freedom.delivery.model.Client
import com.freedom.delivery.model.Order
import com.freedom.delivery.model.OrderOuterClass
import com.freedom.delivery.model.Status

object OrderProtoUtil {
    fun toProto(order: Order): OrderOuterClass.Order {
        return OrderOuterClass.Order.newBuilder()
            .setName(order.name)
            .setPrice(order.price)
            .setStatus(OrderOuterClass.Status.valueOf(order.status.name))
            .setSessionId(order.sessionId)
            .build()
    }

    fun fromProto(order : OrderOuterClass.Order): Order {

        return try {
            Order(
                null,
                order.name,
                order.price,
                Status.valueOf(order.status.name),
                order.sessionId
            )
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid status value in the proto message")
        }
    }
}