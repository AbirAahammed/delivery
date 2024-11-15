package com.freedom.delivery.service

import com.freedom.delivery.config.OrderKafkaStreamConfig
import com.freedom.delivery.model.OrderOuterClass
import com.freedom.delivery.model.Status
import jakarta.annotation.PostConstruct
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OrderProcessing(
    private val orderKafkaStreamConfig : OrderKafkaStreamConfig
) {
    private val logger = LoggerFactory.getLogger(OrderProcessing::class.java)

    @PostConstruct
    fun startStream() {
        val builder = StreamsBuilder()
        val stream: KStream<String, OrderOuterClass.Order> = builder.stream("order")

        val transformedStream: KStream<String, OrderOuterClass.Order> = stream.mapValues { order -> order.toBuilder().setStatus(OrderOuterClass.Status.PENDING).build() }


        // Write the transformed data to the output topic
        transformedStream.to("processed_orders")

        // Build the Kafka Streams application
        val streams = KafkaStreams(builder.build(), orderKafkaStreamConfig.kafkaStreamsProperties)

        // Add a state listener for logging when the stream is connected
        streams.setStateListener { newState, oldState ->
            if (newState == KafkaStreams.State.RUNNING) {
                logger.info("Order application processing started.")
            } else {
                logger.info("Kafka order Streams application is in transition from $oldState to $newState.")
            }
        }

        // Start the Kafka Streams application
        streams.start()

        // Add shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(Thread { streams.close() })
    }

}