// 2025 05 26

// https://sparkbyexamples.com/kafka/apache-kafka-consumer-producer-in-scala/

// APACHE kafka A distributed streaming platform


// In this Scala & Kafa tutorial, you will learn
// how to write Kafka messages to Kafka topic (producer)
// and read messages from topic (consumer) using Scala example;
// producer sends messages to Kafka topics in the form of records,
// a record is a key-value pair along with topic name
// and consumer receives a message from a topic.


// Start zookeeper with the default configuration

// Kafka comes with the Zookeeper built-in,
// all we need is to start the service with the default configuration.
bin/zookeeper-server-shart.sh config/zookeeper.properties


// Star Kafka broker with the default configuration
bn/kafka-server-start.sh config/server.properties


// Create a Kafka topic "text_topic"

bin/kafka-topics.sh --create --zookeeper localhost:2181 \
                    --replication-factor 1 --partition 1 \
                    --topic text_topic


// Kafka Maven Dependency

<dependency>
    <groupId>org.apache.kafka.</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>2.1.0</version>
</dependency>


// Kafka Producer Scala example

// This Kafka Producer scala example publishes messages to a topic as a Record.
// Record is a key-value pair where the key is optional and value is mandatory.
// In this example we ave key and value as string hece, we are using StringSerializer.
// In case if you have a key as a long value
// then you should use LongSerializer, the same applies for valu as-well.

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
object KafkaProducerApp extends App {
    val props:Properties = new Properties()
    props.put("bootstrap.servers","localhost:9092") 
    props.put("key.serializer",
            "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer",
            "org.apache.kafka.common.serialization.StringSerializer")
    props.put("acks","all")
    val producer = new KafkaProducer[String, String](props)
    val topic = "text_topic"
    try {
        for (i e.printStackTrace())
    }finally {
        producer.close()
    }
}

// Producer send method returns metadata where we can find;
// which partition message has written to and offset







