// 2025 03 07 
// https://sparkbyexamples.com/spark/spark-batch-processing-produce-consume-kafka-topic/

// Spark SQL Batch Processing - Produce and Consume Apache Kafka Topic


// This article describes Spark SQL Batch Processing using Apache Kafka Data Source on DataFrame.
// Unlike Spark structure stream processing, we may need to process batch jobs that consume the messages
// from Apache Kafka topic and produces messages to Apache Kafka topic in batch mode.
// To do this we should use read instead of readStream similarly write instead of writeStream on DataFrame.

// Spark SQL Batch Processing - Producing Messages to Kafka Topic.
// Spark SQL Batch Processing - Consuming Messages from Kafka Topic. 


// Maven Dependency for Apache Kafka:
<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-sql-kafka-0-10_2.11</artifactId>
    <version>2.4.0</version>
</dependency>
