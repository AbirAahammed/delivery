package com.freedom.delivery.config

import org.apache.kafka.streams.StreamsConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class KafkaStreamsConfig {

    @Value("\${kafka.streams.application-id}")
    private val applicationId: String? = null

    @Value("\${kafka.streams.bootstrap-servers}")
    private val bootstrapServers: String? = null

    @Value("\${kafka.streams.default-key-serde}")
    private val keySerde: Class<*>? = null

    @Value("\${kafka.streams.default-value-serde}")
    private val valueSerde: Class<*>? = null

    val kafkaStreamsProperties: Properties
        get() {
            val props = Properties()
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId)
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, keySerde)
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, valueSerde)
            return props
        }
}