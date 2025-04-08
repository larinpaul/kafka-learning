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
// * org.springframework.kafka.listener.MessageListenerContainer - 
// An abstraction used to instantiate Kafka message listener containers.
// * org.springframework.kafka.listener.KafkaMessageListenerContainer -
// An implementation class used to create a single-threaded message listener container
// * org.springframework.kafka.listener.ConcurrentMessageListenerContainer -
// An implementation class used to create one or more KafkaMessageListenerContainers based on concurrency
// * org.springframework.kafka.config.KafkaListenerContainerFactory -
// An abstract factory for MessageListenerContainers
// * org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory - 
// An implementation class used to create a ConcurrentMessageListenerContainer


// 3. Installation and Setup

// To download and install Kafka, please refer to the official guide here. // https://kafka.apache.org/quickstart
// We also need to add the spring-kafka dependency to our pom.xml:
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
    <version>3.3.1</version>
</dependency>

// And configure the spring-boot-maven-plugin as follows:
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <mainClass>com.baeldung.spring.kafka.KafkaApplication</mainClass>
    </configuration>
</plugin>

// We can find the latest version of this artifact here. // https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka
// Our example application will be a Spring Boot application.
// We assume that the server has started using the default configuration
// and that we have not changed any server ports.


// 4. Configuring Topics

// Previously, we ran command-line tools to create Kafka "topics":
$ bin/kafka-topics.sh --create
  --zookeeper localhost:2181
  --replication-factor 1 --partitions 1
  --topic mytopic

// But with the introduction of AdminClient in Kafka,
// we can now create topics programmatically.
// We need to add the KafkaAdmin Spring bean,
// which will automatically add topics for all beans of type NewTopic.

@Configuration
public class KafkaTopicConfig {
    
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.pur(AdminClientConfig.BOOTSTAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("baeldung", 1 (short) 1);
    }

}


// 5. Producing Messages

// To create messsages, we first need to configure a ProducerFactory. // http://docs.spring.io/spring-kafka/api/org/springframework/kafka/core/ProducerFactory.html
// This sets the strategy for creating Kafka Producer instances. // https://kafka.apache.org/0100/javadoc/org/apache/kafka/clients/producer/Producer.html

// Then, we need a KafkaTemplate, // https://docs.spring.io/spring-kafka/api/org/springframework/kafka/core/KafkaTemplate.html
// which wraps a Producer instance and provides convenience methods for sending messages to Kafka topics.


// Producer instances are thread-safe.
// So, using a single instance throughout an application context
// will give higher performance.
// Consequently, KafkaTemplate instances are also thread-safe,
// and the use of one instance is recommended.


// 5.1. Producer Configuration

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            bootstrapAddress);
        configProps.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class);
        configProps.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}

// 5.2. Publishing Messages

// We can send messages using the KafkaTemplate class:

@Autowired
private KafkaTemplate<String, String> kafkaTemplate;

public void sendMessage(String msg) {
    kafkaTemplate.send(topicName, msg);
}

// The send API returns a CompletableFuture object.
// If we want to block the sending thread and get the result about the sent message,
// we can call the get API of the CompletableFuture object.
// The thread will wait for the result, but it will slow down the producer.

// Kafka is a fast-stream processing platform.
// Therefore, it's better to handle the results asynchronously
// so that the sbsequent messages do not wait for the result of the previous message.

// We can do this through a callback:
public void sendMessage(String message) {
    CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
    future.whenComplete((result, ex) -> {
        if (ex == null) {
            System.out.println("Sent message=[" + message +
                "] with offset=[" + result.getRecordMetadate().offset() + "]");
        } else {
            System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
        }
    });
}


// 6. Consuming Messages

// 6.1. Consumer Configuration

// To consume messages, we need to configure a ConsumerFactory and a KafkaListenerContainerFactory.
// Once these beans are available in the Spring Bean factory, POJO-based consumers
// can be configured using @KafkaListener annotation.

// @EnableKafka annotation is required on the configuration class 
// to enable the detection of @KafkaListener annotation on spring-managed beans:

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
            bootstrapAddress
        );
        props.put(
            ConsumerConfig.GROUP_ID_CONFIG,
            groupId
        );
        props.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class
        );
        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class
        );
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
            kafkaListenerContainerFactory() {
        
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}


// 6.2. Consuming Messages

// Let's configure a POJO-based listener, also called consumer,
// using @KafkaListener annotation:

@KafkaListener(topics = "topicName", groupId = "foo")
public void listenGroupFoo(String message) {
    System.out.println("Received Message in group foo: ");
}

// We can implement multiple listeners for a topic,
// each with a different group ID.
// Furthermore, one consumer can listen for messages from various topics:

@KafkaListener(topics = "topic1, topic2", groupId = "foo")

// Spring also supports retrieval of one or more messages
// using the @Header annotation in the listener: // http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/messaging/handler/annotation/Header.html

@KafkaListener(topics = "topicName")
public void listenWithHeaders(
    @Payload String message,
    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        System.out.println(
            "Received Message: " + "message "
            + "from partition: " + partition);
}


// 6.3. Consuming Messages from a Specific Partition

// Notice that we created the topic baeldung wiht only one partition.

// For a topic with multiple partitions, howener,
// a @KafkaListener can explicitly subscribe to a particular partition
// of a topic with an initial offset:

@KafkaListener(
    topicPartitions = @TopicPartition(topic = "topicName",
    partitionOffsets = {
        @PartitionOffset(partition = "0", initialOffset = "0"),
        @PartitionOffset(partition = "3", initialOffset = "3")}),
    containerFactory = "partitionKafkaListenerContainerFactory")
public void listenToPartition(
    @Payload String message,
    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        System.out.println(
            "Received Message: " + message +
            "from partition: " + partition
        );
}

// Since the initialOffset has been set to 0 in this listener, 
// all the previously consumer messages from partitions 0 and 3
// will be re-consumer every time this listener is initialized.

// If we don't need to set the offset, we can use the partitions property of @TopicPartition annotation
// to set only the partitions without the offset:

@KafkaListener(topicPartitions
    = @TopicPartitions(topic = "topicName", partitions = { "0", "1" }))


// 6.4. Adding Message Filter for Listeners

// We can configure listeners to consume specific message content by adding a custom filter.
// This can be done by setting a RecordFileStrategy to the KafkaListenerContainerFactory.

@Bean
public ConcurrentKafkaListenerContainerFactory<String, String>
        filterKafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, String> factory =
                    new ConcurrentKafkaListenerContainerFactory<>();
                    factory.setConsumerFactory(consumerFactory());
                    factory.setRecordFilterStrategy(
                        record -> record.value().contains("World"));
            return factory;
        }

// We can configure a listener to use this container factory:

@KafkaListener(
    topics = "topicName",
    containerFactory = "filterKafkaListenerContainerFactory")
    public void listenWithFilter(String message) {
        System.out.println("Received Message in filtered listener: " + message);
    }

// The listener discards all the messages matching the filter.


// 7. Custom Message Converters

// So far, we have only covered sending and receiving Strings as messages.
// However, we can also send and receive custom Java objects.
// This requires configuring the appropriate serializer in ProducerFactory
// and a deserializer in ConsumeFactory.

// Let's look at a simple bean class, which we will send as messages:

public class Greeting {

    private String msg;
    private String name;

    // standard getters, setters and constructor

}


// 7.1. Producting Custom Messages

// In this example, we will use JsonSerializer. // http://docs.spring.io/spring-kafka/api/org/springframework/kafka/support/serializer/JsonSerializer.html
// Let's look at the code for ProducerFactory and KafkaTemplate:

@Bean
public ProducerFactory<String, Greeting> greetingProducerFactory() {
    // ...
    configProps.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        JsonSerializer.class
    );
    return new DefaultKafkaProducerFactory<>(configProps);
}

@Bean
public KafkaTemplate<String, Greeting> greetingKafkaTemplate() {
    return new KafkaTemplate<>(greetingProducerFactory());
}

// We can use this new KafkaTemplate to send the Greeting message:

kafkaTemplate.send(topicName, new Greeting("Hello," "World"));


// 7.2. Consuming Custom Messages

// Similarly, let's modify the ConsumerFactory and KafkaListenerContainerFactory 
// to deserialize the Greeting message correctly:

@Bean
public ConsumerFactory<String, Greeting> greetingConsumerFactory() {
    // ...
    return new DefaultKafkaConsumerFactory<>(
        props,
        new StringDeserializer(),
        new JsonDeserializer<>(Greeting.class)
    );

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Greeting>
            greetingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Greeting> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(greetingConsumerFactory());
        return factory;
    }

}


// The spring-kafka JSON serializer and deserializer use the Jackson libreary, // https://www.baeldung.com/jackson
// which is also an optional Maven dependency for the spring-kafka project.

// So let's add it to our pom.xml:

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.18.2</version>
</dependency>


// Instead of using the latest version of Jackson, it's recommended to use the version
// that is added to the pom.xml of spring-kafka.

// Finally, we need to write a listener to consume Greeting messages:

@KafkaListener(
    topics = "topicName",
    containerFactory = "greetingKafkaListenerContainerFactory")
    public void greetingListener(Greeting greeting) {
        // process greeting message
    }


// 8. Multi-Method Listeners

// Let's now see how we can configure our application to send various kinds of objects 
// to the same topic and then consume them

// First, we'll add a new class, Farewell:

public class Farewell {

    private String message;
    private Integer remainingMinutes;

    // standard getters, setters and constructor

}

// We'll need some extra configuration to be able to send both Greeting and Farewell objects
// to the same topic.


// 8.1. Set Mapping Types in the Producer

// In hte producer, we have to configure the JSON type mapping: // https://www.baeldung.com/java-json

configProps.put(JsonSerializer.TYPE_MAPPINGS, "greeting:com.baeldung.spring.kafka.Greeting, farewell:com.baeldung.spring.kafka.Farewell");

// This way, the library will fill in the type header 
// with the corresponding class name.
// As a result, the ProducerFactory and KafkaTemplate look like this:

@Bean
public ProducerFactory<String, Object> multiTypeProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTRSTAP_SERVERS_CONFIG, bootstrapAddress);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configProps.put(JsonSerializer.TYPE_MAPPINGS, "greeting:com.baeldung.spring.kafka.Greeting, farewell:com.baeldung.spring.kafka.Farewell");
    return new DefaultKafkaProducerFactory<>(configProps);  
}

@Bean
public KafkaTemplate<String, Object> multiTypeKafkaTemplate() {
    return new KafkaTemplate<>(multiTypeProducerFactory());
}

// We can use this KafkaTemplate to send a Greeting, Farewell,
// or any Object to the topic: // https://www.baeldung.com/java-classes-objects
multiTypeKafkaTemplate.send(multiTypeTopicName, new Greeting("Greetings", "World"));
multiTypeKafkaTemplate.send(multiTypeTopicName, new Farewell("Farewell", 25));
multiTypeKafkaTemplate.send(multiTypeTopicName, "Simple string message");


// 8.2. Use a Custom MessageConverter in the Consumer

// To be able to deserialize the incuming message,
// we'll need to provide our Consumer with a custom MessageConverter.

// Behind the scenes, the MessageConverter relies on a Jackson2JavaTypeMapper.
// By default, the mapper infers the type of the received objects:
// on the contrary, we need to tell it explicitly to use the type header
// to determine the target class for deserialization:

typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);

// We also need to provide the reverse mapping information.
// Finding "greeting" in the type header identifies a Greeting object,
// whereas "farewell" corresponds to a Farewell object:

Map<String, Class<?>> mappings = new HashMap<>();
mappings.put("greeting", Greeting.class);
mappings.put("farewell", Farewell.class);
typeMapper.setIdClassMapping(mappings);

// Lastly, we need to configure the packages trusted by the mapper.
// We have to make sure that it contains the location of the target classes:

typeMapper.addTrustedPackages("com.baeldung.spring.kafka");

// As a result, here is the final definition of this MessageConverter:

@Bean
public RecordMessageConverter multiTypeConverter() {
    StringJsonMessageConverter converter = new StringJsonMessageConverter();
    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
    typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
    typeMapper.addTrustedPackages("com.baeldung.spring.kafka");
    Map<String, Class<?>> mappings = new HashMap<>();
    mappings.put("greeting", Greeting.class);
    mappings.put("farewell", Farewell.class);
    typeMapper.setIdClassMapping(mappings);
    converter.setTypeMapper(typeMapper);
    return converter;
}





