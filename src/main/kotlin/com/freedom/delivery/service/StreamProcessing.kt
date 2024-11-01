package com.freedom.delivery.service

import com.freedom.delivery.config.KafkaStreamsConfig
import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.NetworkClient
import org.apache.kafka.clients.consumer.internals.ConsumerCoordinator
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.KStream

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StreamProcessing(
    private val kafkaStreamsConfig: KafkaStreamsConfig
) {

    private val logger = LoggerFactory.getLogger(StreamProcessing::class.java)
    @PostConstruct
    fun startStream() {
        val builder = StreamsBuilder()

        // Define the input topic
        val stream: KStream<String, String> = builder.stream("pending")

        // Example transformation: convert each message to uppercase
        val transformedStream: KStream<String, String> = stream.mapValues { value -> value.uppercase() }

        // Write the transformed data to the output topic
        transformedStream.to("processing")

        // Build the Kafka Streams application
        val streams = KafkaStreams(builder.build(), kafkaStreamsConfig.kafkaStreamsProperties)

        // Add a state listener for logging when the stream is connected
        streams.setStateListener { newState, oldState ->
            if (newState == KafkaStreams.State.RUNNING) {
                logger.info("Kafka Streams application is connected and running.")
            }
            else {
                logger.info("Kafka Streams application is in transition from $oldState to $newState.")
            }
        }

        // Start the Kafka Streams application
        streams.start()

        // Add shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(Thread { streams.close() })
    }
}
