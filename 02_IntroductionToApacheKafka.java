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

// We can differentiate Kafka clients into consumers and producers.
// Producers send messages to Kafka, while consumers receive messages from Kafka.
// They only receive messages by actively polling from Kafka.
// Kafka itself is acting passivley.
// This allows each consumer to have a uique performance without blocking Kafka.

// ...

// Consumers are part of a Consumer Group that Kafka identifies by a simple name.
// Only one consumer of a consumer group will receive a message.
// This allows scaling out consumers with the guarantee of only-once message delivery.


// 4.2. Messages

// A message (we can also name it "record" or "event", depending on the use caes)
// is the fundamental unit of data that Kafka processes.
// Its payload can be of any binary format as well as text formats like plain text, Avro, XML, or JSON.

// ...

// SerDes // There are built-in SerDes, // https://kafka.apache.org/documentation/streams/developer-guide/datatypes.html
// ut we can implement custom SerDes too.

// ..

// 4.3. Topics & Partitions

// A topic is a logical channel or category to which producers publish messages.

// ...

// By default, the retention policy of a topic is 7 days, i.e. after 7 days,
// Kafka deletes the messages automatically,
// independent of delivering to consumers or not.
// We can configure this if necessary. // https://www.baeldung.com/kafka-message-retention

// ...

// Topics consist of partitions (at least one).

// ...

// Kafka can provide both ordering guarantees and load balancing over a pool of consumer processes.

// One consumer will be assigned to one partition when it subscribes to the topic,
// e.g. with the Java Kafka client API, as we have already seen:

String topic = "my-first-topic";
consumer.subscribe(Arrays.asList(topic));

// However, for a consumer, it is possible to choose the partition(s) it wants to poll messages from:

TopicPartition myPartition = new TopicPartition(topic, 1);
consumer.assign(Arrays.asList(myPartition));

// The disadvantage of this variant is that all group consumers have to use this,
// so automatically assigning partitions to group consumers 
// won't work in combination with single consumers that connect to a special partition.
// Also, rebalancng is not possible in case of architectural changes like adding further consumers to the group.

// Ideally, we have as many consumers as partitions,
// so that every consumer can be assigned to exactly one of the partitions, as shown below:

// ...

// If we have more consumers than partitins, those consumers won't receive messages from any partition:

// ...

// If we have fewer consumers than partitions, consumers will receive messages from multiple partitions,
// which conflicts with optimal load balancing:

// ...

// Each producer has its own partitioner, so if we want to ensure that messages are partitioned consistently within the topic,
// we have to ensure that the partitioners of all producers work the same way,
// or we should only work with a single producer.
// ...


// 4.4. Clusters and Partition Replicas

// ...

// We typically do not use a single Kafka Broker, but a Cluster of multiple brokers.

// ...

// For example, using the Kafka CLI, we could create a topic with 6 partitions, each of them syncronized on 3 brokers:
sh kafka-topics.sh --bootstrap-server localhosh:9092 --create --topic my-replicated-topic --partitions 6 -- replication-factor 3

// For example, a replication factor of three means, that the cluster is resilient for up to two replica failures (N-1 resiliency).
// We have to ensure that we have at least as many borkers as we specify as the replication factor.
// Otherwise, Kafka does not create the topic until the count of brokers increases.

// For better efficiency, replication of a partition only occurs in one direction.
// ... 

// Partition leading is distributed to multiple brokers.
// ...

// Kafka uses Kraft (in earlier versions: Zookeeper) for the orchestration of all brokers within the cluster. // https://www.baeldung.com/kafka-shift-from-zookeeper-to-kraft


// 4.4. Putting All Together

// If we put producers and consumers together with a cluster of three brokers that manage a single topic
// with three partitions and a replication factor of 3, we'll get this architecture:

// picture


// 5. Ecosystem

// ... 

// We already know that multiple clients like a CLI, a Java-based client with integration to Spring applications,
// and multiple GUI tools are available to connect with Kafka.
// Of course, there are further Client APIs for other programming language (e.g., C/C++, Python, or Javascript),
// but those are not part of the Kafka project.

// Built on tops of these APIs, there are furhter APIs for special purposes.


// 5.1. Kafka Connect API

// Kafka Connect is an API for exchanging data with third-party systesm. // https://www.baeldung.com/kafka-connectors-guide
// There are existing connectors // https://docs.confluent.io/platform/current/connect/kafka_connectors.html
//  e.g. for AWS S3, JDBC, or even for exchanging data between different Kafka clusters.
// And of course, we can write custom connectors too.


// 5.2. Kafka Streams API
// https://www.baeldung.com/java-kafka-streams
// Kafka Streams is an API for implementing stream processing applications
// that get their input from a Kafka topic, and store the result in another Kafka topic.


// 5.3. KSQL
// KSQL is an SQL-like interface built on top of Kafka Streams.
// ... uses SQL-like syntax to define stream processing of messages that are exchanged with Kafka.
// For this, we use ksqlDB, which connects to the Kafka cluster. // https://www.baeldung.com/ksqldb


// 5.4. Kafka REST Proxy
//  https://github.com/confluentinc/kafka-rest
// The Kafka REST proxy provides a RESTful interface to a Kafka cluster.
// This way, we do not need any Kafka clients and avooid using the native Kafka protocol.
// It allows web frontend to connect with Kafka and makes it possible
// to use network components like API gateways or firewalls.


// 5.5. Kafka Operators for Kubernetes (Strimzi)
// https://strimzi.io/
// Strimzi is an open-source project that provides a way to run Kafka on Kubernetes and OpenShift platforms.
// It introduces custom Kubernetes resources making it easier to declare and manage Kafka-related resources
// in a Kubernetes-native way.
// It follows the Operator Pattern, // https://kubernetes.io/docs/concepts/extend-kubernetes/operator/
// i.e. operators automate tasks like provisioning, scaling, rolling, updates, and monitoring of Kafka clusters.


// 5.6. Cloud-based Managed Kafka Services

// Kafka is available as a managed service on the commonly used cloud platforms:
// Amazon Managed Streaming for Apache Kafka (MSK), // https://aws.amazon.com/msk/
// Managed Service - Apache Kafka on Azure,
// and Google Cloud Managed Service for Apache Kafka. // https://console.cloud.google.com/managedkafka/clusters?pli=1

// 6. Conclusion

// In this article, we have learned that Kafka is designed for high scalability and fault tolerance.
// Producers collect messages and send them in batches,
// topics are divided into partitions to allow parallel message delivery and load balancing of consumers,
// and replication is done over multiple brokers to ensure fault tolerance.
