// https://www.baeldung.com/spring-kafka

// 1. Overview

// Apache Kafka is a distributed and fault-tolerant stream processing system.

// In this tutorial, we'll cover Spring support for Kafka and its abstraction level
// over native Kafka Java client APIs.

// Spring Kafka brings the simple and typical Spring template programming model
// with a KafkaTemplate and Message-driven POJOs via @KafkaListener annotation.


// 2. What is a Listener Container is Spring for Apache Kafka?

// The Spring Framework implements the Inversion of Control (IoC) principle via Dependency Injection (DI).
// Objects define their dependencies directly, and an IoC container injects them when creating a bean.
// Beans are objects instantiated, assembled, and managed by a Spring IoC container.
// In other words, 
// a container is an application context responsible for instantiating, configuring, and assembling the beans.

// In the context of Apahe Kafka, a listener container is a container that contains a consumer,
// or listener, of Kafka messages.
// Further, Spring for Apache Kafka uses a container factory to create message listener containers.
// We use the @KafkaListener annotation to designate a bean method as a message listener 
// for a listener container.
// Accordingly, a container factory creates listener containers for bean methods annotated with @KafkaListener.
// The Spring for Apache Kafka frameworkprovides interfaces and classes to manage the instantiation of listener containers:




