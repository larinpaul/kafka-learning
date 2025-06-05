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


