// 2025 03 16

// Configuring Message Retention Period in Apache Kafka

// Reviewed by Eric Martin

// https://www.baeldung.com/kafka-message-retention


// 1. Overview

// When a producer sends a message to Apache Kafka, it appends it in a log file
// and retains it for a configured duration.
// In this tutorial, we'll learn to configure time-based message retention properties for Kafka topics.


// 2. Time-Based Retention

// ...


// 3. Server-Level Configuration

// ...

// we can tube configuring exactly one of three time-based configuration properties:
// * log.retention.hours
// * log.retention.minutes
// * log.retention.ms // Will take precedence because Kafka overrides a lower-precision value with a higher one.


// 3.1. Basics

// First, let's inspect the default value for retention by executing the grep command // https://www.baeldung.com/linux/grep-sed-awk-differences#grep // The grep command searches for lines matching a regex expression and prints those matching lines to the standard output. It is useful when we need a quick way to find out whether a particular pattern exists or not in the given input. // grep [OPTION] PATTERN [FILE...]
// from the Apache Kafka directory: // https://kafka.apache.org/documentation/#quickstart




