// 2025 05 23

// Kafka's Shift from ZooKeeper to Kraft
// https://www.baeldung.com/kafka-shift-from-zookeeper-to-kraft
// Last updated: Feruary 13, 2024

// 1. Introduction 

// Kafka, in its architecture, has recently shifted from ZooKeeper to a quorum-based
// controller that uses a new consensus protocol called Kafka Raft,
// shortened as Kraft (pronouncedd as "craft").

// In this tutorial, we'll examine the reson why Kafka has taken this decision
// and how the change simplifies its architecture
// and makes it more powerful to use.


// 2. Brief Overview of ZooKeeper

// ZooKeeper // https://zookeeper.apache.org/
// is a service that enables highly reliable distributed coordination.
// It was originally developer at Yahoo! to streamline processess
// running on big data clusters.
// It started as sub-project of Hadoop but later became a standalone 
// Apache Foundation project in 2008.
// It's widely used to serve several use cases in large distributed systems.




