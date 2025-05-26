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
