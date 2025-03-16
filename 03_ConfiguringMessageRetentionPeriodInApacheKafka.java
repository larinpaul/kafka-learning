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






