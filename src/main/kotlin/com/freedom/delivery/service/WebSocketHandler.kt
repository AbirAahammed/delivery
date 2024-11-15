package com.freedom.delivery.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.freedom.delivery.model.SocketMessage
import com.freedom.delivery.model.Tag
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap


@Component
class WebSocketHandler : TextWebSocketHandler() {
    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions[session.id] = session

        // Create an instance of ObjectMapper
        val objectMapper = ObjectMapper()

        // Create a module to register the custom serializer
        val module = SimpleModule().addSerializer(SocketMessage::class.java, SocketMessageSerializer())

        // Register the module
        objectMapper.registerModule(module)

        // Create a SocketMessage instance
        var payload = SocketMessage(Tag.INFRASTRUCTURE, session.id)

        // Serialize the SocketMessage to JSON
        val jsonString = objectMapper.writeValueAsString(payload)


        this.sendMessageToClient(session.id, jsonString )
        println("Connected: " + session.id)
    }

    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        println("Received: $payload")
        session.sendMessage(TextMessage("Echo: $payload"))
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
        println("Disconnected: " + session.id)
    }

    @Throws(IOException::class)
    fun sendMessageToClient(sessionId: String, message: String?) {
        val session = sessions[sessionId]
        if (session != null && session.isOpen) {
            session.sendMessage(TextMessage(message!!))
        }
    }

    companion object {
        var sessions: ConcurrentHashMap<String, WebSocketSession> = ConcurrentHashMap()
    }
}