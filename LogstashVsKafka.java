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




