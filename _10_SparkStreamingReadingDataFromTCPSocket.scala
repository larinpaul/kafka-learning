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

