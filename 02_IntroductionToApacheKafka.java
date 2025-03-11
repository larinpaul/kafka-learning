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

// ...

// We can setup this cointainer-internal port forwarding. e.g. using socat.
// We have to install it within the container (Alpine Linux),
// so we need toconnect to the container's bash with root permissions.
// So we need these commands, beginning within the host system's command line:

# Connect to the container's bash (find out the name with 'docker ps')
docker exec -it --user=root <name-of-kafka-ui-container> /bin/sh
# Now, we are connected to teh container's bash.
# Let's install 'socat'
apk add socat
# Use socat to create the port forwarding
socat tcp-listen:9092,fork tcp:host.docker.internal:9092
# This will lead to a running process that we don't kill as long as the container's running     '

// Accordingly, we need to run socat each time we start the container.
// Another possibility would be to provide an extension to the Dockerfile. //https://github.com/provectus/kafka-ui/blob/master/kafka-ui-api/Dockerfile

// Now, we can specify localhost:9092 as the bootstrap server within the Kafka UI
// and should be able to view and create topics, as shown below:


// 3.5. Use Kafka Java Client

// We have to add the following Maven dependency to our project: // https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>3.9.0</version>
</dependency>

// We can then connect to Kafka and consume the messages we produced before:

// specify connection properties
Properties props = new Properties();
props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
props.put(ConsumerConfig.GROUP_ID_CONFIG, "MyFirstConsumer");
props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
// receive messages that were sent before the consumer started
props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
// create the consumer using props.
try (final Consumer<Long, String> consume = new KafkaConsumer<>(props)) {
    // subscribe to the topic.
    final String topic = "my-first-topic";
    consumer.subscribe(Arrays.asList(topic));
    // poll messages from the topic and print them to the console
    consumer
        .poll(Duration.ofMinutes(1))
        .forEach(System.out::println);
}

// Of course, there is an integration for the Kafka Client in Spring. // https://www.baeldung.com/spring-kafka

// 4. Basic Concept

