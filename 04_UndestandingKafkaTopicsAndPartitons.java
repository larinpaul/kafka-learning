// 2025 04 18


// https://www.baeldung.com/kafka-topics-partitions
// Understanding Kafka Topics and Partitons

// Last updated: May 11, 2024


// 1. Introduction
// In this tutorial, we'll explore Kafka topics and partitions and how they relate to each other // https://www.baeldung.com/spring-kafka


// 2. What Is a Kafka Topics

// A topic is a storage mechanism for a sequence of events.
// Eventually, topics are durable log files that keep events in the same order as ther occur in time.
// So, each new event is a always added to the end of the log.
// Additionally, events are immutable.
// Thus, we can't change them after they've been added to a topic.

// An example use case for Kafka topics is recording a sequence of temperature measurements for a room.
// Once a temperature value has been recorded, like 25 C at 5.02 PM,
// it cannot be altered as it has already occurred.
// Furthermore, a temperature value at 5:06 PM cannot precede the one recorded at 5:02 PM.
// Hence, by treating each temperature measurement as an event,
// a Kafka topic would be a suitable option to store that data.


// 3. What Is a Kafka Partition

// Kafka uses topic partitioning to improve scalability.
// In partitioning a topic, Kafka breaks it into fractions and stores each of them
// in different nodes of its distributed system.
// That number of fractions is determined by us or by the cluster default configurations.

// Kafka guarantees the order of the evenets within thesame topic partition.
// However, by default, it does not guarantee the order of events across all partitions.

// For example, to imporve performance, we can divide the topic into two different partitions
// and read from them on the consumer side.
// In that case, a consumer reads the events in the same order they arrived at the same partiton.
// In contract, if Kafka delivers two events to different partitions,
// we can't guarantee that the consumer reads the events in the same order they were produced.

// To improve the ordering of events, we can se an event key to the event object. // https://www.baeldung.com/java-kafka-message-key
// With that, events with the same key are assigned to the same partition, which is ordered.
// Thus, events with the same key arrive at the consumer side in the same order they were produced.


// 4. Consumer Groups

// A consumer group is a set of consumers that reads from a topics.
// Kafka divides all partitions among the consumers in a group,
// where any given partitionis always consumed once by a group member.
// However, that division might be unbalanced,
// which means that more than one partition can be assigned to a consumer.

// For instance, let's picture a topic with three partitions that a consumer group with two consumers should read.
// Hence, once possible division is that the first consumer gets partitions one and two,
// and the second consumer only gets partition three.

// In the KIP-500 update, // https://cwiki.apache.org/confluence/display/KAFKA/KIP-500%3A+Replace+ZooKeeper+with+a+Self-Managed+Metadata+Quorum
// Kafka introduced a new consensus algorithm // https://www.baeldung.com/cs/consensus-algorithms-distributed-systems
// named KRaft. // https://www.baeldung.com/kafka-shift-from-zookeeper-to-kraft
// As we add consumers to a group or remove consumers from a group,
// KRaft rebalances the partitions between the remanining consumers proportionally.
// Thus, it guarantees that there's no partition without a consumer assigned.


// 5. Configure the Application

// In this section, we'll create the classes to configure a topic, consumer and producer service.

// 5.1. Topic Configuration

// First, let's create the configuration class for our topic:

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    public NewTopic celciusTopic() {
        return TopicBuilder.name("celcius-scale-topic")
                .partitions(2)
                .build();
    }
}

// The KafkaTopicConfig class injects two Spring beans.
// The KafkaAdmin bean initiates the Kafka cluster with the network address it should run,
// while the NewTopic bean creates a topic named celcius-scale-topic with one partition.


// 5.2. Consumer and Producer Configuration

// We need the necessary classes to inject the producer and consumer configurations for our topic.
// First, let's create the producer configuration class:

public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, Double> producerFactory() {
        Map<String, Object> configProps= new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DoubleSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Double> kafkaTemplate() {
        return new KafkaTemplate<>(producerFacotry());
    }
}

// The KafkaProducerConfig injects two Spring beans.
// The ProducerFactory tells how Kafka is supposed to serialize events
// and whichserver the producer should listen to.
// The KafkaTemplate will be used in the consumer service class to create events.


// 5.3. Kafka Producer Service

// Finally, after the initial configuration, we can create the driver application.
// Let's first create the producer application:






