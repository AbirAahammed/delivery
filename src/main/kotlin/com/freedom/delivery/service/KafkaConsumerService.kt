package com.freedom.delivery.service

import com.fasterxml.jackson.databind.JsonSerializable
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.freedom.delivery.model.OrderOuterClass
import com.freedom.delivery.model.SocketMessage
import com.freedom.delivery.model.Tag
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Service
class KafkaConsumerService {

    private val logger: Logger = LoggerFactory.getLogger(KafkaConsumerService::class.java)

    @Autowired
    lateinit var socket : WebSocketHandler;

    @KafkaListener(topics = ["processed_orders"], groupId = "order-group")
    fun consumeMessage(record: ConsumerRecord<String, OrderOuterClass.Order>) {
        val key = record.key()
        val message = record.value()

        // Deserialize the byte array to an `Order` object using your utility class
        val order = OrderProtoUtil.fromProto(message)
        var payload = SocketMessage(Tag.COMMUNICATION, Json.encodeToString(order))

        // Create an instance of ObjectMapper
        val objectMapper = ObjectMapper()

        // Create a module to register the custom serializer
        val module = SimpleModule().addSerializer(SocketMessage::class.java, SocketMessageSerializer())

        // Register the module
        objectMapper.registerModule(module)
        val jsonString = objectMapper.writeValueAsString(payload)

        socket.sendMessageToClient(order.sessionId,  jsonString)
        logger.info("Consumed message with key: $key")
        logger.info("Order details: $order")
    }
}