public class _06_IntroductionToKafkaStreamsInJava {
    
    // https://www.baeldung.com/java-kafka-streams

    // Introduction to KafkaStreams in Java

    // Last updated: January 8, 2024
    // Written by: baeldung

  
    // 1. Overview

    // In this article, we'll be looking at the KafkaStreams library. // https://kafka.apache.org/documentation/streams/

    // KafkaStreams is engineerd by the creators of Apache Kafka.
    // The primary goal of this piece of software is to allow programmers
    // to create efficient, real-time streaming applications that could work as Microservices.

    // KafkaStreams enables us to consume from Kafka topics,
    // analyze or transform data, and potentially, send it to another Kafka topic.

    // To demonstrate KafkaStreams, we'll create a simple application that reads sentences
    // from a topic, counts occurrences of words and prints the count per word.

    // Important to note is that the KafkaStreams library isn't reactive
    // and has no support for async operations and backpressure handling.


    // 2. Maven Dependency

    // To start writing Stream processing logic using KafkaStreams, 
    // we need to add a dependency to kafka-streams // https://mvnrepository.com/artifact/org.apache.kafka/kafka-streams
    //and kafka-clients: // https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients

    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artiactId>kafka-streams</artifactId>
        <version>3.4.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>3.4.0</version.
    </dependency>

    // We also need to have Apache Kafka installed and started 
    // because we'll be using a Kafka topic.
    // This topic will be the data source for our streaming job.

    // We can download Kafka and other required dependencies from the official websote. // https://www.confluent.io/download/
    


}
