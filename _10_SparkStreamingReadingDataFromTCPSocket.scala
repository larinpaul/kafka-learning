// 2025 05 29

// https://sparkbyexamples.com/spark/spark-streaming-from-tcp-socket/

// Naveen Nelamali // Apache Spark // March 27, 2024


// Data receives to TCP socket from sources => [TCP socket] => Data streams from TCP socket => Spark Streaming => Batches of input Data => Spark Engine => Batches of processed Data to S3, HDFS, HBase etc. => S3, HDFS, HBase, Kafka, Others

// Using Spark streaming we will see a working example of how to read data from TCP Socket,
// process it and write output to console.
// Spark uses readStream() to read and writeStream() to write streaming DataFrame or Dataset.
// The below-explained example does the word count on streaming data and outputs the result to console.


