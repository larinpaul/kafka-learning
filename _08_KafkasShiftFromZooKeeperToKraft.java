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


// 2.1. ZooKeeper Architecture

// ZooKeeper stores data in a hierarchical namespace, similar to a standard file system.
// The namespaceconsists of data registers called znodes. // https://www.baeldung.com/java-zookeeper

// A name is a sequence of path elements separated by a slash.

// Every node in the namespace is identified by a path:
// https://www.baeldung.com/wp-content/uploads/2022/11/ZooKeeper-Data-Model.jpg

// There can be three types of znodes in a ZooKeeper namespace:
// * The first is persistent, which is the default type and remains in ZooKeeper until deleted.
// * The second is ephemeral, which gets deleted if the session in which the znode
// was created disconnects.
// Also, ephemeral znodes can't have children.
// * The third is sequential, which we can use to create sequential numbers like IDs.

// Through its simple architectgure, ZooKeeper offers a reliable system 
// with fast processing and scalability.
// It's intended to be replicated over a set of servers called an ensemble.
// Each server maintains an in-memory image of the state,
// along with a transition log and snapshots in a persistent store:
// https://www.baeldung.com/wp-content/uploads/2022/11/ZooKeeper-Architecture.jpg

// ZooKeeper clients connect to exactly one server but can failover to another
// if the server becomes unavailable.
// Read requests are serviced from the local replica of each server database.
// Wrtie requests are processed by an agreement protocol.
// This involves forwarding all such requests to the leader server,
// which coordinates them using the ZooKeeper Atomic Broadcast (ZAB) protocol. // https://zookeeper.apache.org/doc/r3.4.13/zookeeperInternals.html#sc_atomicBroadcast

// The clients enable applications to read, write, and process
// streams of events in parallel, at scale, and in a fault-tolerant manner.
// Producers are the client applications that publish events to Kafka.
// At the same time, consumers are those that sbuscribe to these events from Kafka.


// 3.2. The Role of ZooKeeper
// ... a lot of text...


// 3.3. Problems with ZooKeeper
// ... a lot of text ...


// 4. Kafka Raft (Kraft) Protocotl
// ... a lot of text ...


// 5. A Simplified and Better Kafka!
// ... a lot of text ...


// 6. Conclusion
