// 2025 04 18


// https://www.baeldung.com/kafka-topics-partitions
// Understanding Kafka Topics and Partitons

// Last updated: May 11, 2024


// 1. Introduction
// In this tutorial, we'll explore Kafka topics and partitions and how they relate to each other // https://www.baeldung.com/spring-kafka


// 2. What Is a Kafka Topics

// A topic is a storage mechanism for a sequence of events.
// Eventually, topics are durable log files that keep events in the same order as ther occur in time.
// So, each new event is a always added to the end of the log.
// Additionally, events are immutable.
// Thus, we can't change them after they've been added to a topic.

// An example use case for Kafka topics is recording a sequence of temperature measurements for a room.
// Once a temperature value has been recorded, like 25 C at 5.02 PM,
// it cannot be altered as it has already occurred.
// Furthermore, a temperature value at 5:06 PM cannot precede the one recorded at 5:02 PM.
// Hence, by treating each temperature measurement as an event,
// a Kafka topic would be a suitable option to store that data.





