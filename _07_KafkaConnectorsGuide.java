// 2025 05 03

// 1. Overview

// Apache Kafka® is a distributed streaming platform.
// In a previous tutorial, we discussed 
// how to implement Kafka consumers and producers using Spring. // https://www.baeldung.com/spring-kafka

// In this tutorial, we'll learn how to use Kafka Connectors.

// We'll have a look at:
// * Different types of Kafka Connectors
// * Featuresand modes of Kafka Connect
// * Connectors configuration using property files as well as the REST API.


// 2. Basics of Kafka Connect and Kafka Connectors

// Kafka Connect is a framework for connecting Kafka with external systems
// such as databases, key-value stores, search indexes, and file systesm,
// using so-called Connectors.

// Kafka Connectors are ready-to-use components, which can help us to import data
// from external systes into Kafka topics
// and export data from Kafka topics into external systems.
// We can use existing connector implementations for common data sources
// and sinks or implement our own connectors.

// A source connector collects data from a system.
// Source systems can be entire databases, streams tables, or message brokers.
// A source connector could also collect metrics from application servers into Kafka topics,
// making the data available for stream processing with low latency.

// A sink connector delivers data from Kafka topics into other systems,
// which might be indexes such as Elasticsearch, batch systems such as Hadoop,
// or any kind of database.

// Some connectors are maintained by the community, 
// while others are supported by Confluent or its partners.
// Really, we can find connectors for most popular systems, 
// like S3, JDBC, and Cassandra, just to name a few.


// 3. Features

// Kafka Connect features include:

// * A framework for connecting external systems with Kafka - it simplifies the development,
// deployment, and management of connectors
// * Distributed and standalone modes - it helps us to deploy large clusters
// by leveraging the distributed nature of Kafka,
// as well as setup for development, testing, and small production deployments
// * REST interface - we can manage connectors using a REST API
// * Automatic offset management - Kafka Connect helps us to handle the offset commit process,
// which saves us the trouble of implementing this error-prone part of connector development manually
// * Distributed and scalable by deffault - Kafka Connect uses the existing group management protocol;
// we can add more workers to scale up a Kafka Connect cluster
// * Streaming and batch integration - Kafka Connect is an ideal solution 
// for bridging streaming and batch data systems in connection 
// with Kafka's existing capabilities
// * Transformations - these enable us to make simple and lightweight
// modifications to individual messages


// 4. Setup

// Instead of using the plain Kafka distribution,
// we'll download Confluent Platform, a Kafka distribution prvided by Confluent, Inc.,
// the company behind Kafka.
// Concluent Platfrom comes with some additional tools and clients,
// compared to plain Kafka,
// as well as some additional pre-built Connectors.

// For our case, the Open Source edition is sufficient,
// which can be found at Confluent's site. // https://www.confluent.io/download/


// 5. Quick Start Kafka Connect

// For starters, we'll discuss the principle of Kafka Connect,
// using its most basic Connectors, which are
// the file SOURCE connector and the file SINK connector.

// Conveniently, Confluent Platform comes with both of these connectors,
// as well as reference configurations.


// 5.1. Source Connector Configuration

// For the source connector, the reference configuration 
// is available at $CONFLUENT_HOME/etc/kafka/connect-file-source.properties:
name=local-file-source
connector.class=FileStreamSource
tasks.max=1
topic=connect-test
file=test.txt

// This configuration has some properties that are common for all source connectors:
// * name is a user-specified name for the connector instance
// * connector.class specifies the implementing class, basically the kind of connector
// * task.max specifies how many instances of our source connector should run in parallel, and
// * topic defines the topic to which the connector should send the output.

// In this case, we also have a connector-specific attribute:
// * file defines the file from which the connector should read the input

// For this to work then, let's create a basic file with some context:

echo -e "foo\nbar\n" > $CONFLUENT_HOME/test.txt

// Note that the working directory is $CONFLUENT_HOME.


// 5.2. Sink Connector Configuration

// For our sink connector, we'll use the reference configuration
// as $CONFLUENT_HOME/etc/kafka/connect-file-sink.properties:
name=local-file-sink
connector.class=FileStreamSink
tasks.max=1
file=test.sink.txt
topics=connect-test

// Logically, it contains exactly the same parameters,
// though this time connector.class specifies the sink connector implementation,
// and files is the location where the connector should write the content.


// 5.3. Worker Config

// Finally, we have to configure the Connect worker,
// which will integrate our two connectors 
// and do the work of reading from the source connector and writing to the sink connector.

// For that, we canuse $CONFLUENT_HOME/etc/kafka/connect-standalone.properties.

bootstrap.servers=localhost:9092
key.converter=org.apache.kafka.connect.json.JsonConverter
value.converter=org.apache.kafka.connect.json.JsonConverter
key.converter.schemas.enable=false
value.converter.schemas.enable=false
offset.storage.file.filename=/tmp/connect.offsets
offset.flush.interval.ms=10000
plugin.path=/share/java




