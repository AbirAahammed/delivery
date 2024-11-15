package com.freedom.delivery.service

import com.freedom.delivery.model.Order
import com.freedom.delivery.model.OrderOuterClass
import com.freedom.delivery.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*


@Service
class KafkaProducerService(
    private val kafkaTemplate: KafkaTemplate<String, OrderOuterClass.Order>,
    private val orderRepository: OrderRepository) {

    val logger: Logger = LoggerFactory.getLogger(KafkaProducerService::class.java)

    fun sendMessage(topic: String, order: Order, key: String? = null) {
        val message = OrderProtoUtil.toProto(order)
        val generatedKey = key ?: UUID.randomUUID().toString()

        logger.info("Sending message to topic: $topic")
        logger.info("Message: $message")
        kafkaTemplate.send(topic, generatedKey, message)
        order.id = generatedKey
        orderRepository.save(order)
    }
}