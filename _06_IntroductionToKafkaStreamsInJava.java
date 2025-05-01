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


    // 3.Configuring KafkaStreams Input

    // The first thing we'll do is the definitionof the input Kafka topic.

    // We can use the Confluent tool that we donwloaded - it contains a Kafka Server.
    // It also contains the kafka-console-producer that we can use to publish messages to Kafka

    // To get started let's run our Kafka cluster:

    ./confluent start

    // Once Kafka starts, we can define our data source and name our application
    // using APPLICATION_ID_CONDIF:

    String inputTopic = "inputTopic";

    Properties streamsConfiguration = new Properties();
    streamsConfiguration.put(
        StreamsConfig.APPLICATION_ID_CONFIG,
        "wordcount-live-test"
    );

    // A crucial configuration parameter is the BOOTSTRAP_SERVER_CONFIG.
    // This is the URL to our local Kafka instance that we just started:
    
    private String bootstrapServers = "localhost:9092";
    streamsConfiguration.put(
        StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
        bootstrapServers
    );

    // Next, we need to pass the type of the key and value of messages
    // that will be consumed from inputTopic:
    
    streamsConfiguration.put(
        StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
        Serdes.String().getClass().getName()
    );
    streamsConfiguration.put(
        StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
        Serdes.String().getClass().getName());

    // Stream processing is often stateful.
    // When we want to save intermediate results, 
    // we need to specify the STATE_DIR_CONFIG parameter.

    // In our test, we're using a local file system:
    
    this.stateDirectory = Files.createTempDirectory("kafka-streams");
    streamsConfiguration.put(
        StreamsConfig.STATE_DIR_CONFIG, this.stateDirectory.toAbsolutePath().toString());


    // 4. Building a Streaming Topology

    // Once we defined our input topic, we can create a Streaming Topology -
    // that is a definitionof how events should be handled and transformed.

    // In our example, we'd like to implement a word counter.
    // For every sentence sent to inputTopic, 
    // we want to split it into words and calculate the occurrence of every word.

    // We can use an instance of the KStreamsBuilder class to start constructing our topology:

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> textLines = builder.stream(inputTopic);
    Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);

    KTable<String, Long> wordCounts = textLines
        .flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
    .groupBy((key, word) -> word)
    .count();

    // To implement word count, firstly, we need to split the values using the regular expression.

    // The split method is returning an array.
    // We're using the flatMapValues() to flatten it.
    // Otherwise, we'd end up with a list of arrays,
    // and it'd be inconvenient to write code using such structure.

    // Finally, we're aggregating the values for every word
    // and calling the count() that will calculate occurrences of a specific word.



}
