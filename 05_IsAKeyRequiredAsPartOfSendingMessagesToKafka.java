public class 05_IsAKeyRequiredAsPartOfSendingMessagesToKafka {
    
    // 2025 04 27

    // https://www.baeldung.com/java-kafka-message-key

    // Last updated: January 8, 2024

    // Written by: Avin Buricha
    // Reviewed by: Saajan Nagendra


    // 1. Intorudction

    // Apache Kafka is an open-source and distributed stream processing system // https://www.baeldung.com/spring-kafka
    // that is fuault-tolerant and provides high troughput.
    // Kafka is basically a messaging system that implements a publisher-subscriber model.
    // The messaging, storage, and stream processing capabilities of Kafka allow us to store
    // and analyze realt-time data streams at scale.

    // In this tutorial, we'll first look at the significaance of a key in a Kafka message.
    // We'll then learn how we can publish messages with a key to a Kafka topic.

    
    // 2. Significance of a Key in a Kafka Message

    // As we know, Kafka effectively stores a stream of records in the order in which we generate records.

    // When we publish a message to a Kafka topic, it's distributed among the available partitions
    // in a round-robin fashion. Hence, within a Kafka topic, the order of messages
    // is guaranteed within a partition but not across partitions.

    // When we publish messages with a key to a Kafka topic, all messages with the same key 
    // are guaranteed to be stored in the same partition by Kafka.
    // Thus keys in Kafka messages are useful if we want to maintain order for messages having the same key.

    // To summarize, keys aren't mandatory as part of sending messages to Kafka.
    // Basically, if we wish to maintain a strict order of messages with the same key,
    // then we should definitely be using keys with messages.
    // For all other cases, having null keys will provide a better distribution of messages amongst the partitions.

    // Next, let's straightaway deep dive int osome of the implementation code
    // having Kafka messages with a key.


    // 3. Setup
    
    // Before we begin, let's first initialize a Kafka cluster, set up the dependencies,
    // and initialize a connection with the Kafka cluster.

    // Kafka's Java library provides easy-to-use Producer and Consumer API
    // that we can use to publish and consume message from Kafka.


    // 3.1. Dependencies

    // Firstly, let's add the Maven dependency for Kafka Clients Java library // https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
    // to our project's pom.xml file:
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>3.4.0</version>
    </dependency>


    // 3.2. Cluster and Topic Initialization

    // Secondly, we'll need a running Kafka cluster to which we can connect
    // and perform various Kafka operations.
    // The guide assumes that a Kafka cluster is running on our local system 
    // with the default configurations.

    // Lastly, we'll create a Kafka topic with multiple partitions 
    // that we can use to publish and consume messages.
    // Referring to our Kafka Topic Creation guide, let's create a topic named "baeldung":

    Properties adminProperties = new Properties();
    adminProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

    Admin admin = Admin.create(adminProperties);

    // Here, we created an instance of Kafka's Admin with the basic configurations
    // defined by the Properties instance. // https://www.baeldung.com/java-properties
    // Next, we'll use this Admin instance to create a topic named "baeldung" with five partitions:

    admin.createTopics(Collections.singleton(new NewTopic("baeldung", 5, (short), 1)));

    // Now that we have the Kafka cluster setup initialized with a topic,
    // let's publish some messages with a key.


    // 4. Publishing Messages With a Key

    // To demonstrate our coding examples, we'll first create an instance of KafkaProducer
    // with some basic producer properties defined by the Properties instance.
    // Next, we'll use the created KafkaProducer instance to publish messages
    // with a key and verify the topic partition.

    // Let's deep dive into each of these steps in detail.


    // 4.1. Initialize Producer

    // First, let's create a new Properties instance that holds the producer's properties
    // to connect to our local broker:

    Properties producerProperties = new Properties();
    producerProperties.put(ProducerConfig.BOOTSTRP_SERVERS_CONFIG, "localhost:9092");
    producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // Further, let's create an instance of KafkaProducer using the created producer's Properties instance:

    KafkaProducer <String, String> producer = new KafkaProducer<>(producerProperties);

    // The KafkaProducer class's constructor accepts a Properties object (or a Map)
    // and returns an instance of KafkaProducer.


    // 4.2. Publish Messages

    // The Kafka Publisher API provides multiple constructors to create an instance of ProducerRecord with a key.
    // We use the ProducerRecord<K,V>(String topic, K key, V value) constructor to create a message with a key:

    ProducerRecords<String, String< records = new ProducerRecord<>("baeldung", "message-key", "Hello World");

    // Here, we created an instance of ProducerRecord for the "baeldung" topic with a key.

    // Now let's publish a few messages to the Kafka topic and verify the partitions.


    for (int i = 1; i <= 10; i++) {
        ProducerRecord<String, String> records = new ProducerRecord<>("baeldung", "message-key", String.valueOf(i));
        Future<RecordMetadata> future = producer.send(record);
        RecordMetadata metadata = future.get();

        logger.info(String.valueOf(metadata.partition()));
    }


}
