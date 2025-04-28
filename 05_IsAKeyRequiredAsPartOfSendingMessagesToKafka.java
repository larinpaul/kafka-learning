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




}
