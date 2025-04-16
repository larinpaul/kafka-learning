// 2025 04 14

// https://www.baeldung.com/logstash-kafka-differences

// Last updated: October 22, 2024 


// 1. Overview

// Logstash and Kafka are two powerful tools for managing real-time data streams.
// While Kafka excels as a distributed event streaming platform,
// Logstash is a data processing pipeline for ingesting, filtering, and forwarding data to various outputs.

// In this tutorial, we'll examine the difference between Kafka and Logdash in more detail 
// and provide examples of their usage.


// 2. Requirements

// Before learning the difference between Logstash and Kafka, let's ensure we have a few
// prerequisites installed and basic knowledge of the technologies involved.
// First, we need to install Java 8 or later. // https://docs.oracle.com/en/java/javase/17/install/overview-jdk-installation.html

// Logstash is part of the ELK stack (Elasticsearch, Logstash, Kibana) // https://www.elastic.co/guide/en/elastic-stack/current/installing-elastic-stack.html
// but can be installed and used independently.
// For Logstash, we can visit the official Logstash download page // https://www.elastic.co/downloads/logstash
// and download the appropriate package for our operatiing system (Linux, macOS, or Windows).

// We also need to install Kafka // https://www.baeldung.com/apache-kafka
// and have confidence in our understandng of the publisher-subscriber model. // https://www.baeldung.com/cs/publisher-subscriber-model


// 3. Logstash

// Let's look at the main Logstash components and a command-line example to process a log file.

// 3.1. Logstash Components

// Logstash is an open-source data processing pipeline 
// with the ELK Stack used to collect, process, and forward data from multiple sources.
// It's composed of several core components that work together
// to collect, transform and output data:
// 1. Inputs: These bring data into Logstash from various sources 
// such as log filed, databases, message queues like Kafka, or cloud services.
// Inputs define where the raw data comes from.
// 2. Filters: These components process and transform the data. 
// Common filters include Grok for pasring unstructured data, // https://www.elastic.co/guide/en/logstash/current/plugins-filters-grok.html
// mutate for modifying fields, and date for timestamp formatting.
// Filters allow for deep customization and data preparation before sending it to its final destination.
// 3. Outputs: After processing, outputs send the data to destinations 
// such as Elasticsearch, databases, message queues, or local files.
// Logstash supports multiple parallel outputs,
// making it ideal for distributing data to various endpoints.
// 4. Codecs: Codes encode and decode data streams, // https://www.elastic.co/guide/en/logstash/current/codec-plugins.html
// such as converting JSON to structured object or reading plain text.
// They act as mini-plugins that process the data as it's being ingested or sent out.
// 5. Pipelines: A pipeline is a defined data flow through inputs, filters, and outputs.
// Pipelines can create complext workflows, enabling data processing in multiple stages. 

// These components work together to make Logstash as powerful tool
// for centralizing logs, transforming data, and integrating with various external systems.


// 3.2. Logstash Example

// Let's give an example of how we process an input file to an output in JSON format.
// Let's create an example.log input file in the /tmp directory:

2024-10-12 10:01:15 INFO User login successful
2024-10-12 10:04:32 ERROR Database connection failed
2024-10-12 10:10:45 WARN Disk space running low

// We can then run the logstash -e command by providing a configuration:

$ sudo logstash -e '
input {
    file {
        path => "/tmp/example.log"
        start_position => "beginning"
        sincedb_path => "/dev/null"
    }
}
filter {
    grok {
        match => { "message" => "%{TIMESTAMP_ISO8601:timestamp} %{LOGLEVEL:loglevel} %{GREEDYDATA:message}" }
    }
    mutate {
        remove_field => ["log", "timestamp", "event", "@timestamp"]
    }
}
output {
    file {
        path => "/tmp/processed-logs.json"
        codec => json_lines
    }
}'

// Let's explain the different paths of the configuration:
// * The whole chain of commands (input/filter/output) is a pipeline.
// * Extract timestamp, log level, and message fields from the logs with the grok filter.
// * Remove unnecessary info with a *mutate* filter.
// * Apply JSON format with Codec in the *output* filter.
// * After the input example.log file in processed,
// the output will be encoded in JSON format in the processed-log.json file. 


// Let's see an output example:
{
    "message":["2024-10-12 10:05:32 ERROR Database connection failed", "Database connection failed"],
    "host":{"name":"baeldung"},"@version":"1"
}
{
    "message":["2024-10-12 10:10:45 WARM Disk space running low","Disk space running low"],
    "host":{"name":"baeldung"},"@version":"1"
}
{
    "message":["2024-10-12 10:01:14 INFO User login successful","User login successful"],
    "host":{"name":"baeldung"},"@version":"1"
}

// As we cansee, the output file is JSON withadditional info, such as the @version,
// that we can use, for example, to document the change and ensure that any downstream processes
// (like querying in Elasticsearch) are aware of it to maintain data consistency.


// Apache Kafka is an open-source distributed event streaming platform for building 
// real-time data pipelines and applications.
// Let's look at it's main components:
// 1. Topics and Partitions: Kafka organizes messages into categories called topics. // https://www.baeldung.com/kafka-topics-partitions
// Each topic is divided into partitions, which allow data to be processed on multiple servers
// in parallel. For example, in an e-commenrce application,
// you might have separate topics for order data, payment transactions, and user activity logs.
// 2. Producers and Consumers: Producers publish data (messages) to Kafka topics,
// while consumers are applications or serviec that read and process these messages.
// Producers push data to Kafka's distributed brokers, ensuring scalability,
// while consumers can subscribe to topics and read messages from specific partitions.
// Kafka guarantees that consumers read each message in order.
// 3. Brokers: Kafka borkers are servers that store and manage topic partitions.
// Multiple brokers comprise a Kafka cluster, distributing data and ensuring fault tolerance.
// If one broker fails, other brokers take over thedata, providing high availability.
// 4. Kafka Streams and Kafka Connect. // https://www.baeldung.com/java-kafka-streams // https://www.baeldung.com/kafka-connectors-guide
// Kafka Streams is a powerful stream processing library that allows real-time data processing
// directly from Kafka topics. This, it enables applications to process and transform data on the fly,
// such as calculating real-time analytics or detecting patterns in financial transactions.
// On the other hand, Kafka Connect simplifies the integration of Kafka with external systems.
// It provides connectors for integrating databases, cloud services, and other applications.
// 5. ZooKeeper and KRaft: // https://www.baeldung.com/kafka-shift-from-zookeeper-to-kraft
// Traditionally, Kafka used ZooKeeper for distibuted comfiguration management,
// including managing broker metadata and leader election for partition replication.
// With the introduction of KRaft (Kafka Raft), Kafka now supports ZooKeeper-less architectues,
// but ZooKeeper is still commonly used in many setups.

// Together, these components enable Kafka to deliver a scalable, fault-tolerant,
// distributed messaging platform that can handle massive volumes of streaming data.


// 4.2. Kafka Example

// Let's create a topic, publish a simple "Hello, World" message, and consumer it.

// First, let's create a topic. It can belong to multiple partitons
// and typically represents one subject of our domain:

$ /bin/kafka-topics.sh \
    --create \
    --topic hello-world \
    --bootstrap-server localhost:9092 \
    --partitions 1 \
    --replication-factor 1

// We'll get the message of the topic creation:
$ Created topic hello-world.

// Let's now try to send a message to the topic:
$ /bin/kafka-console-producer.sj \
    --topic hello-world \
    --bootstrap-server localhost:9092 \
    <<< "Hello, World!"

// Now, we canconsumer our messages:
$ /bin/kafka-console-consumer.sh \
    --topic hello-world \
    --from-beginning \
    --bootstrap-server localhost:9092

// We'll get messages from the Kafka log storage for that specific topic by consuming them:
// Hello, World!



