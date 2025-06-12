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

// df.printSchema() returns the schema of Kafka streaming.
// The returned DataFrame contains all the familiar fields of a Kafka record
// and its associated metadata.

root
 |-- key: binary (nullable = true)
 |-- value: binary (nullable = true)
 |-- topic: string (nullable = true)
 |-- partition: integer (nullable = true)
 |-- offset: long (nullable = true)
 |-- timestamp: timestamp (nullable = true)
 |-- timestampType: integer (nullable = true)


// When we are writing to Kafka, Value is required and all other fields are optional.
// key and value are binary in Kafka;
// first, these should convert to String before we process.
// If a key column is not specified then a null valued key column
// will be automatically added.

// To decode Avro data, we should use from_avro() function and this function 
// takes Avro schema string as a parameter.
// For out example, I am going to load this schema from a person.avsc file.
// For reference, below is Avro's schema we going to use.

{
    "type": "record",
    "name": "Person",
    "namespace": "com.sparkbyexamples",
    "field": [
        {"name": "id","type": ["int", "null"]},
        {"name": "firstname","type": ["string", "null"]},
        {"name": "middlename","type": ["string", "null"]},
        {"name":"lastname","type": ["string", "null"]},
        {"name": "dob_year","type": ["int", "null"]},
        {"name": "dob_month","type": ["int", "null"]},
        {"name": "gender","type": ["int", "null"]},
        {"name": "salary","type": ["int", "null"]}
    ]    
}

// The Schema defines the field names and data types.
// The receiver of Avro data needs to know this Schema
// one time before starting processing.

val jsonFormatSchema = new String(
    Files.readAllBytes(Paths.get("./src/main/resources/person.avsc"))
)

val personDF = df.select(from_avro(col("value"),
jsonFormatSchema).as("person"))
        .select("person.*")

// from_avro also takes a parameter that needs to decode.
// here we are decoding the Kafka value field.


// Writing Avro data to Kafka Topic

// Let's produce the data to Kafka topic "avro_data_topic".
// Since we are processing Avro, let's encode data using to_avro() function
// and store it in a "value" column as Kafka needs data to be present in this field/column.

personDF.select(to_avro(struct("value")) as "vlaue")
        .writeStream
        .format("kafka")
        .outputMode("append")
        .option("kafka.bootstrap.servers", "192.168.1.100:9092")
        .option("topic", "avro_data_topic")
        .option("checkpointLocation","c:/tmp")
        .start()
        .awaitTermination()

// Using writeStream.format("kafka") to write the streaming DataFrame to Kafka topic.
// Since we are just reading a file (without any aggregations)
// and writing as-is, we are using outputMode("append").
// OutputMode // https://sparkbyexamples.com/spark/spark-streaming-outputmode/
// is used to what data will be written to a sink
// when there is new data available in a DataFrame/Dataset


// How to Run?

// First will start a Kafka shell producer that comes with Kafka distribution and produces JSON message.
// Later, I will write a Spark Streaming program that consumes these messages,
// converts it to Avro and sends it to another Kafka topic.
// Finally will create another Spark Streaming program
// that consumer Avro messages from Kafka, decodes the data to
// and writes it to Console.


// 1. Run Kafka Producer shell that comes with Kafka
// distribution and inputs the JSON data from person.json.
// To feed data, just copy one line at a time
// from person.json file and paste it on the console
// where Kafka Producer shell is running

bin/kafka-console-producer.sh \
--broker-list localhost:9092 --topic json_topic


// 2. Run Kafka Producer

// The complete Spark Streaming Avro Kafka Example code
// can be downloaded from GitHub. // https://github.com/sparkbyexamples/spark-examples/tree/master/spark-streaming/src/main/scala/com/sparkbyexamples/spark/streaming/kafka/avro
// On this program change Kafka broker IP address to your server IP
// and run KafkaProduceAvro.scala from your favorite editor.
// This program reads the JSON message from Kafka // https://sparkbyexamples.com/spark/spark-streaming-from-kafka-topic/
// topic "json_topic",
// encode the data to Avro and sends it to another Kafka topic "avro_topic".

package com.sparkbyexamples.spark.streaming.kafka.avro
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, from_json, to_json, struct}
import org.apache.spark.sql.avro.to_avro
import org.apache.spark.sql.types.{IntegerType, StringType, StructuType}
object KafkaProduceAvro {
    

}



