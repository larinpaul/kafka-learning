// 2025 03 09

// https://www.baeldung.com/apache-kafka

// Introduction to Apache Kafka


// 1. Overview
// In this tutorial, we'll learn the basics of Kafka - the use caes and core concepts anyone should know.
// We can then find and understand more detailed articles about Kafka // https://www.baeldung.com/tag/kafka


// 2. What Is Kafka?

// https://kafka.apache.org/
// Kafka is an open-source stream processing platform developed by the Apache Software Foundation.
// We can use it as a messaging system to decouple message producers and consumers,
// but in comparison to "classical" messaging systems like ActiveMQ,
// it is designed to handle real-time data streams and provides
// a distributed, fault-tolerant, and highly scalable architecture for processing and storing data.

// Therefore, we can use it in various use cases:
// * Real-time data processing and analytics
// * Log and event data aggregation
// * Monitoring and metrics collection
// * Clickstream data analytics
// * Fraud detection
// * Stream processing in big data pipelines


// 3. Setup A Local Environment

// If we deal with Kafka for the first time, we might like to have a local installation
// to experience its features. We could get this quickly with the help of Docker.


// 3.1. Install Kafka

// We download an existing image and run a container instance with this command:
docker run -p 9092:9092 -d bash79/kafka-kraft // https://hub.docker.com/r/bashj79/kafka-kraft

// This will make the so-called Kafka broker available on the host system at port 9092.
// Now, we would like to connect to the broker using a Kafka client. 
// There are multiple clients that we can use.





