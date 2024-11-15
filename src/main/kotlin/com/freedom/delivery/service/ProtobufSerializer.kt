package com.freedom.delivery.service


import org.apache.kafka.common.serialization.Serializer
import com.freedom.delivery.model.OrderOuterClass.Order // Ensure this matches your generated class package

class ProtobufSerializer : Serializer<Order> {
    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {}

    override fun serialize(topic: String?, data: Order?): ByteArray? {
        return data?.toByteArray() // Converts the protobuf object to a byte array
    }

    override fun close() {}
}
