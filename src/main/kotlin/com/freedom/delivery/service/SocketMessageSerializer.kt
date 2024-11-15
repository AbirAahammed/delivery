package com.freedom.delivery.service

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.freedom.delivery.model.SocketMessage

class SocketMessageSerializer : JsonSerializer<SocketMessage>() {
    override fun serialize(value: SocketMessage, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("tag", value.tag.name)
        gen.writeStringField("message", value.message)
        gen.writeEndObject()
    }

}