package com.freedom.delivery.config

import com.freedom.delivery.service.ProtobufDeserializer
import com.freedom.delivery.service.ProtobufSerializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.StreamsConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.util.Properties
import com.freedom.delivery.model.OrderOuterClass.Order // Ensure this matches your generated class package
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.Deserializer

@Configuration
class OrderKafkaStreamConfig {

    @Value("\${kafka.streams.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Value("\${kafka.streams.default-key-serde}")
    private val keySerde: Class<*>? = null


    val kafkaStreamsProperties: Properties
        get() {
            val props = Properties()
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "order-stream")
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, keySerde)
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, OrderSerde::class.java)
            return props
        }
}

// Define a Serde for Order using Protobuf
class OrderSerde : Serde<Order> {
    override fun serializer(): Serializer<Order> = ProtobufSerializer() // Use the ProtobufSerializer
    override fun deserializer(): Deserializer<Order> = ProtobufDeserializer() // Use the ProtobufDeserializer
}

