// https://www.baeldung.com/spring-kafka

// 1. Overview

// Apache Kafka is a distributed and fault-tolerant stream processing system.

// In this tutorial, we'll cover Spring support for Kafka and its abstraction level
// over native Kafka Java client APIs.

// Spring Kafka brings the simple and typical Spring template programming model
// with a KafkaTemplate and Message-driven POJOs via @KafkaListener annotation.


// 2. What is a Listener Container is Spring for Apache Kafka?

// The Spring Framework implements the Inversion of Control (IoC) principle via Dependency Injection (DI).
// Objects define their dependencies directly, and an IoC container injects them when creating a bean.
// Beans are objects instantiated, assembled, and managed by a Spring IoC container.
// In other words, 
// a container is an application context responsible for instantiating, configuring, and assembling the beans.

// In the context of Apahe Kafka, a listener container is a container that contains a consumer,
// or listener, of Kafka messages.
// Further, Spring for Apache Kafka uses a container factory to create message listener containers.
// We use the @KafkaListener annotation to designate a bean method as a message listener 
// for a listener container.
// Accordingly, a container factory creates listener containers for bean methods annotated with @KafkaListener.
// The Spring for Apache Kafka frameworkprovides interfaces and classes to manage the instantiation of listener containers:
// * org.springframework.kafka.listener.MessageListenerContainer - 
// An abstraction used to instantiate Kafka message listener containers.
// * org.springframework.kafka.listener.KafkaMessageListenerContainer -
// An implementation class used to create a single-threaded message listener container
// * org.springframework.kafka.listener.ConcurrentMessageListenerContainer -
// An implementation class used to create one or more KafkaMessageListenerContainers based on concurrency
// * org.springframework.kafka.config.KafkaListenerContainerFactory -
// An abstract factory for MessageListenerContainers
// * org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory - 
// An implementation class used to create a ConcurrentMessageListenerContainer


// 3. Installation and Setup

// To download and install Kafka, please refer to the official guide here. // https://kafka.apache.org/quickstart
// We also need to add the spring-kafka dependency to our pom.xml:
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
    <version>3.3.1</version>
</dependency>

// And configure the spring-boot-maven-plugin as follows:
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <mainClass>com.baeldung.spring.kafka.KafkaApplication</mainClass>
    </configuration>
</plugin>

// We can find the latest version of this artifact here. // https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka
// Our example application will be a Spring Boot application.
// We assume that the server has started using the default configuration
// and that we have not changed any server ports.


// 4. Configuring Topics

// Previously, we ran command-line tools to create Kafka "topics":
$ bin/kafka-topics.sh --create
  --zookeeper localhost:2181
  --replication-factor 1 --partitions 1
  --topic mytopic

// But with the introduction of AdminClient in Kafka,
// we can now create topics programmatically.
// We need to add the KafkaAdmin Spring bean,
// which will automatically add topics for all beans of type NewTopic.

@Configuration
public class KafkaTopicConfig {
    
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.pur(AdminClientConfig.BOOTSTAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("baeldung", 1 (short) 1);
    }

}





