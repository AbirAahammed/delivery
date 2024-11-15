package com.freedom.delivery.controller

import com.freedom.delivery.model.Order
import com.freedom.delivery.service.KafkaProducerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
    @Autowired
    lateinit var kafkaProducerService: KafkaProducerService;
    val LOGGER : Logger = LoggerFactory.getLogger(HomeController::class.java)
    @GetMapping("/")
    fun home(): String {
        return "Hello, World!"
    }

    @PostMapping("/order")
    fun order(@RequestBody order: Order) : String {
        LOGGER.info("Order received: ${order.name}, ${order.price}, ${order.status}")
        kafkaProducerService.sendMessage("order", order)
        return "Order received: ${order.name}, ${order.price}, ${order.status}"
    }
}