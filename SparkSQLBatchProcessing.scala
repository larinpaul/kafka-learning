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


// Spark SQL Batch Processing - Producing Messages to Kafka Topic.

package com.sparkbyexamples.spark.streaming.batch
import org.apache.spark.sql.SparkSession
object WriteDataFrameToKafka {

  def main(args: Array[String]): Unit = {

      val spark: SparkSession = SparkSession.builder()
        .master("local[1]")
        .appName("SparkByExamples.com")
        .getOrCreate()

      val data = Seq (("iphone", "2007"),("iphone 3G","2008"),
        ("iphone 3GS","2009"),
        ("iphone 4","2010"),
        ("iphone 4S",2011),
        ("iphone 5","2012"),
        ("iphone 8","2014"),
        ("iphone 10","2017"))

      val df = spark.createDataFrame(data).toDF("key","value")
      /*
        since we are using datagrame which is laready in text,
        selectExprt is optional.
        If the bytes of the Kafka recordsrepresent UTF8 strings,
        we can simply use a cast to convert the binary data
        into the correct type.

        df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      */
      df.write
        .format("kafka")
        .option("kafka.bootstrap.servers","192.169.1.100:9092")
        .option("topic","text_topic")
        .save()
  }
}

