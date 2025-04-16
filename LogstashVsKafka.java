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


