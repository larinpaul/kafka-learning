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

// The Kafka CLI is a part of the installation and is available within the Docker container.
// We can use it by connecting to the container's bash.

// First, we need to find out the container's name with this command:

docker ps

CONTAINER ID   IMAGE                    COMMAND                  CREATED        STATUS       PORTS                    NAMES
7653830053fa   bashj79/kafka-kraft      "/bin/start_kafka.sh"    8 weeks ago    Up 2 hours   0.0.0.0:9092->9092/tcp   awesome_aryabhata

// In this sample, the name is awesome_aryabhata. We then connect to the bash using: // https://www.baeldung.com/linux/shell-alpine-docker
docker exec -it awesome_aryabhata /bin/bash

// Now, we can, for example, create a topic (we'll clarify this term later) and list all existing topics with this commands:

cd /opt/kafka/bin

# create topic 'my-first-topic'
sh kafka-topics.sh --bootstrap-server localhost:9092 --create --topic my-first-topic --partitions 1 -- replication-factor 1

# list topics
sh kafka-topics.sh --bootstap-server localhost:9092 --list

# send messages to the topic
sh kafka-console-producer.sh --bootstrap-server localhost:9092 --topic my-first-topic
>Hello World
>The weather is fine
>I love Kafka


// 3.3. Use KafkaIO GUI

// ...


// 3.4. Use UI for Apache Kafka (Kafka UI)

// The UI for Apache Kafka (Kafka UI) // https://github.com/kafbat/kafka-ui
// is a web UI, implemented with Spring Boot and React,
// and provided as a Docker image for a simple installation as a container // https://hub.docker.com/r/provectuslabs/kafka-ui
// with the following command:
docker run -it -p 8080:8080 -e DYNAMIC_CONFIG_ENABLED=true provectuslabs/kafka-ui

// We can then open the UI in the browser using http://localhost:8080 and define a cluster, as this picture shows:





