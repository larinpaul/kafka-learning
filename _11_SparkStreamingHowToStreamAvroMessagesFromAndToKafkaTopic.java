// 2025 06 04

// https://sparkbyexamples.com/spark/spark-streaming-consume-and-produce-kafka-messages-in-avro-format/

// Spark Streaming - How to Stream Avro messages from/to Kafka topic.

// This article describes Spark Structured Streaming from Kafka in Avro file format
// and usage of from_avro() and to_avro() SQL functions using the Scala programming language


// DATA STREAMING -> kafka -> Apache Spark STRUCTURED STREAMING -> Console|Memory|DB|Files


// * Reading Avro Data from Kafka Topic
// * Writing Avro Data to Kafka Topic
// * How to Run
// ** Running Producer with Example
// ** Running Consumer with Example

// Before deep-diving into this further, let's understand a few points 
// regarding Spark Streaming, Kafka and Avro.

// Spark Streaming is a scalable, high-throughput, fault-tolerant streaming processing system
// that supports both batch and streaming workloads.
// It is an extension of the core Spark API to process real-time data 
// from sources like Kafka, Flume, and Amazon Kinesis to name it a few.
// This processed data can be pushed to databases, Kafka, live dashboards e.t.c

// Apache Kafka is a publish-subscriber messaging system originally written at LinkedIn.
// Accessing Kafka is enabled by using below Kafka client Maven dependency.
<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-sql-kafka-0-10_2.11</artifactId>
    <version>2.4.0</version>
</dependency>

// Apache Avro is a data serialization system, // https://avro.apache.org/docs/++version++/
// it is mostly used in Apache Spark especially for Kafka-based data pipelines.
// When Avro data is stored in a file, its schema is stored with it,
// so that files may be processed later by any program.

// * Avro is best suited for scenarios where data is frequently written
// and schema evolution is important,
// making it iteal for data interchange and streaming applications.

// * Parquet is optimzied for read-heavy opearions and analytical workloads,
// making it a better choice for data warehousing and big data analytics

// Accessing Avro from Spark is enabled by using below Spark-Avro Maven dependency.
// The spark-avro module is external and not included in spark-submit or spark-shell by default.

<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-avro_2.11</artifactId>
    <version>2.4.0</version>
</dependency>


// Prerequisites

// If you don't have Kafka cluster setup. follow the below articles
// to set up the single broker cluster
// and get familiar with creating and describing topics.

// * Install & set-up Kafka Cluster guide // https://sparkbyexamples.com/kafka/apache-kafka-cluster-setup/
// * How to create and describe Kafka topics // https://sparkbyexamples.com/kafka/creating-apache-kafka-topic/


// Reading Avro data from Kafka Topic

// Streaming uses readStream() on SparkSession to load a streaming Dataset.
// option("startingOffsets", "earliest") is used to read all data
// available in the topic at the start/earliest of the query, 
// we may not use this option that often and the default value 
// for straightOffsets is latest which reads only new data that's yet to process.

val df = spark.readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", "192.168.1.100:9092")
        .option("subscribe", "avro_topic")
        .option("startingOffsets", "earliest") // From starting
        .load()

df.printSchema()


