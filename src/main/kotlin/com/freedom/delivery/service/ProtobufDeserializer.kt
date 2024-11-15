package com.freedom.delivery.service

import com.freedom.delivery.model.OrderOuterClass.Order
import org.apache.kafka.common.serialization.Deserializer

class ProtobufDeserializer : Deserializer<Order> {

    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {
        // No specific configuration needed for this deserializer
    }

    override fun deserialize(topic: String?, data: ByteArray?): Order? {
        return if (data != null && data.isNotEmpty()) {
            // Parse the byte array into an Order object using the generated method
            Order.parseFrom(data)
        } else {
            null
        }
    }

    override fun close() {
        // No resources to close
    }
}

