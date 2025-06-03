// 2025 05 29

// https://sparkbyexamples.com/spark/spark-streaming-from-tcp-socket/

// Naveen Nelamali // Apache Spark // March 27, 2024


// Data receives to TCP socket from sources => [TCP socket] => Data streams from TCP socket => Spark Streaming => Batches of input Data => Spark Engine => Batches of processed Data to S3, HDFS, HBase etc. => S3, HDFS, HBase, Kafka, Others

// Using Spark streaming we will see a working example of how to read data from TCP Socket,
// process it and write output to console.
// Spark uses readStream() to read and writeStream() to write streaming DataFrame or Dataset.
// The below-explained example does the word count on streaming data and outputs the result to console.


// Spark Streaming data from TCP Socket

// Use readStream.format("socket") from Spark session object
// to read data from the socket and provide options host and port where you want to stream data from.

val df = spark.readStream
        .format("socket")
        .option("host","localhost")
        .option("port","9090")
        .load()

// Spark reads the data from socket and represents it in a "value" column of DataFrame.
// df.printSchema() outputs

root
 |-- value: string (nullable = true)


// pyspark.sql.functions.explode
// Explode - Returns a new row for each element in the given array or map.


// Process the data using DataFrame operations

// Now let's process the data by counting the work;
// first split the data,
// use the explode to flatten it and apply groupBy function.

val wordDF = df.select(explode(split(df("value")," ")).alias("word"))

val count = wordsDF.groupBy("word").count()


// Spark Streaming to console

// Use writeStream.format("console") to write data to console 
// and outputMode("complete") should use when writing streaming aggregation DataFrames.

val query = count.writeStream
        .format("console")
        .outputMode("complete")
        .start()
        .awaitTermination()


// Source code of Spark Streaming TCP Socket example

package com.sparkbyexample.spark.streaming
import org.apache.spark.sql.SparkStreaming
import org.apache.spark.sql.functions.{explode, split}
object SparkStreamingFromSocket {
  def main(args: Array[String]): Unit = {
    
    val spark:SparkSession = SparkSession.builder()
      .master("local[3]")
      .appName("SparkByExample")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    val df = spark.readStream
      .format("socket")
      .option("host","localhost")
      .option("port","9090")
      .load()

    val wordsDF = df.select(explode(split(df("value")," ")).alias("word"))
    val count = wordsDF.groupBy("word").count()
    val query = count.writeStream
      .format("console")
      .outputMode("complete")
      .start()
      .awaitTermination()
  }
}


// Let's see how to run this example.

// 1. Install NetCat

// First, let's write some data to Socket, using NetCat, use this utility we can write data to TCP socket,
// it is the best utility to write to the socket.
// after install run below command.

nc -l -p 9090

// 2. Run Spark Streaming job.

// The complete example code can also be found at GitHub. // https://github.com/sparkbyexamples/spark-examples/blob/master/spark-streaming/src/main/scala/com/sparkbyexamples/spark/streaming/SparkStreamingFromSocket.scala
// Download it and run SparkStreamingFromSocket.scala from your favorite editor.
// When program execution pauses, switch to NetCat console 
// and type a few sentences and press enter for each line as shown below.

ubuntu@namenode:~$ nc -l -p 7890
My Name is Naveen
I work for Oracle
Oracle is good

// Yields below output on your editor.

-------------------------------------------
Batch: 1
-------------------------------------------
+------+-----+
|  word|count|
+------+-----+
|   for|    1|
|Oracle|    2|
|  Name|    1|
|    is|    2|
|Naveen|    1|
|  work|    1|
|    My|    1|
|     I|    1|
|  good|    1|
+------+-----+

// Conclusion

// You have learned how to stream or read a data from TCP Socket 
// using Spark Structured Streaming with Scala example
// and also learned how to use NetCat to write data to TCP socket.

