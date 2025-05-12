// 2025 05 03

// 1. Overview

// Apache Kafka® is a distributed streaming platform.
// In a previous tutorial, we discussed 
// how to implement Kafka consumers and producers using Spring. // https://www.baeldung.com/spring-kafka

// In this tutorial, we'll learn how to use Kafka Connectors.

// We'll have a look at:
// * Different types of Kafka Connectors
// * Featuresand modes of Kafka Connect
// * Connectors configuration using property files as well as the REST API.


// 2. Basics of Kafka Connect and Kafka Connectors

// Kafka Connect is a framework for connecting Kafka with external systems
// such as databases, key-value stores, search indexes, and file systesm,
// using so-called Connectors.

// Kafka Connectors are ready-to-use components, which can help us to import data
// from external systes into Kafka topics
// and export data from Kafka topics into external systems.
// We can use existing connector implementations for common data sources
// and sinks or implement our own connectors.

// A source connector collects data from a system.
// Source systems can be entire databases, streams tables, or message brokers.
// A source connector could also collect metrics from application servers into Kafka topics,
// making the data available for stream processing with low latency.

// A sink connector delivers data from Kafka topics into other systems,
// which might be indexes such as Elasticsearch, batch systems such as Hadoop,
// or any kind of database.

// Some connectors are maintained by the community, 
// while others are supported by Confluent or its partners.
// Really, we can find connectors for most popular systems, 
// like S3, JDBC, and Cassandra, just to name a few.


// 3. Features

// Kafka Connect features include:

// * A framework for connecting external systems with Kafka - it simplifies the development,
// deployment, and management of connectors
// * Distributed and standalone modes - it helps us to deploy large clusters
// by leveraging the distributed nature of Kafka,
// as well as setup for development, testing, and small production deployments
// * REST interface - we can manage connectors using a REST API
// * Automatic offset management - Kafka Connect helps us to handle the offset commit process,
// which saves us the trouble of implementing this error-prone part of connector development manually
// * Distributed and scalable by deffault - Kafka Connect uses the existing group management protocol;
// we can add more workers to scale up a Kafka Connect cluster
// * Streaming and batch integration - Kafka Connect is an ideal solution 
// for bridging streaming and batch data systems in connection 
// with Kafka's existing capabilities
// * Transformations - these enable us to make simple and lightweight
// modifications to individual messages


// 4. Setup

// Instead of using the plain Kafka distribution,
// we'll download Confluent Platform, a Kafka distribution prvided by Confluent, Inc.,
// the company behind Kafka.
// Concluent Platfrom comes with some additional tools and clients,
// compared to plain Kafka,
// as well as some additional pre-built Connectors.

// For our case, the Open Source edition is sufficient,
// which can be found at Confluent's site. // https://www.confluent.io/download/


// 5. Quick Start Kafka Connect

// For starters, we'll discuss the principle of Kafka Connect,
// using its most basic Connectors, which are
// the file SOURCE connector and the file SINK connector.

// Conveniently, Confluent Platform comes with both of these connectors,
// as well as reference configurations.


// 5.1. Source Connector Configuration

// For the source connector, the reference configuration 
// is available at $CONFLUENT_HOME/etc/kafka/connect-file-source.properties:
name=local-file-source
connector.class=FileStreamSource
tasks.max=1
topic=connect-test
file=test.txt

// This configuration has some properties that are common for all source connectors:
// * name is a user-specified name for the connector instance
// * connector.class specifies the implementing class, basically the kind of connector
// * task.max specifies how many instances of our source connector should run in parallel, and
// * topic defines the topic to which the connector should send the output.

// In this case, we also have a connector-specific attribute:
// * file defines the file from which the connector should read the input

// For this to work then, let's create a basic file with some context:

echo -e "foo\nbar\n" > $CONFLUENT_HOME/test.txt

// Note that the working directory is $CONFLUENT_HOME.


// 5.2. Sink Connector Configuration

// For our sink connector, we'll use the reference configuration
// as $CONFLUENT_HOME/etc/kafka/connect-file-sink.properties:
name=local-file-sink
connector.class=FileStreamSink
tasks.max=1
file=test.sink.txt
topics=connect-test

// Logically, it contains exactly the same parameters,
// though this time connector.class specifies the sink connector implementation,
// and files is the location where the connector should write the content.


// 5.3. Worker Config

// Finally, we have to configure the Connect worker,
// which will integrate our two connectors 
// and do the work of reading from the source connector and writing to the sink connector.

// For that, we canuse $CONFLUENT_HOME/etc/kafka/connect-standalone.properties.

bootstrap.servers=localhost:9092
key.converter=org.apache.kafka.connect.json.JsonConverter
value.converter=org.apache.kafka.connect.json.JsonConverter
key.converter.schemas.enable=false
value.converter.schemas.enable=false
offset.storage.file.filename=/tmp/connect.offsets
offset.flush.interval.ms=10000
plugin.path=/share/java

// Note that plugin.path can hold a list of paths, 
// where connector implementations are available

// As we'll use connectors bundled with Kafka,
// we can set plugin.path to $CONFLUENT_HOME/share/java.
// Working with Windows, it might be necessary to provide an absolute path here.

// For the other parameters, we can leave the default values:
// * bootstrap.servers contains the addresses of the Kafka brokers
// * key.converter and value.converter define converter classes,
// which serialize the data as it flows from the source into Kafka
//and then from Kafka to the sink
// * key.converter.schemas.enable and value.converter.schemas.enable
// are converter-specific settings
// * offset.storage.file.filename is the most important setting
// when running Connect in standalone mode:
// it defines where Connect should store its offset data
// * offset.flush.interval.ms defines the interval at whichthe worker
// tries to commit offests for tasks

// And the list of parameters is quite mature,
// so check the official documentation for a complete list. // http://kafka.apache.org/documentation/#connectconfigs


// 5.4. Kafka Connect in Standalone Mode

// And with that, we can start our first connector setup:

$CONFLUENT_HOME/bin/connect-standalone \
    $CONFLUENT_HOME/etc/kafka/connect-standalone.properties \
    $CONFLUENT_HOME/etc/kafka/connect-file-source.properties \
    $CONFLUENT_HOME/etc/kafka/connect-file-sink.properties

// First of,, we can inspect the content of the topic using the command line:

$CONFLUENT_HOME/bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic connect-test --from-beginning

// As we can see, the source connector took the data from the test.txt file,
// transformed it into JSON, and sent it to Kafka:

{"schema":{"type":"string","optional":false},"payload":"foo"}
{"schema":{"type":"string","optional":false},"payload":"foo"}

// And, if we have a look at the folder $CONFLUENT_HOME,
// we can see that a file test.sink.txt was created here:

cat $CONFLUENT_HOME/test.sink.txt
foo
bar

// As the sink connector extracts the value from the payload attribute
// and writes it to the destination file,
// the data in test.sink.txt has the content of the original test.txt file.

// Now let's add more lines to test.txt.

// When we do, we see that the source connector detects these changes automatically.

// We only have to make sure to insert a newline at the end,
// otherwise, the source connector won't consider the last line.

// At this point, let's stop the Connect process,
// as we'll start Connect in distributed mode in a few lines.


// 6. Connect's REST API

// Until now, we made all configurations by passing property files via the command line.
// However, as Connect is designed to run as a service, 
// there is also a REST API available.

// By default, it is available at http://localhost:8083. A few endppoints are:

// * GET /connectors - returns a list with all connectors in use
// * GET /connectors/{name} - returns details about a specific connector
// * POST /connectors - creates a new connector; the requeset body
// should be a JSON object containing a string name field
// and an object config field with the connector configuration parameters
// * GET /connectors/{name}/status - returns the current status of the connector 
// - including if it is runninf, failed or paused - which worker
// it is assigned to, error information if it has failed,
// and the state of all its tasks.
// * DELETE /connectors/{name} - deletes a connector,
// gracefully stopping all tasks and deleting its configuration
// * GET /connector-plugins - returns a list of connector plugins
// installed in the Kafka Connect cluster

// The official documentation provides a list with all endpoints. // http://kafka.apache.org/documentation/#connect_rest

// We'll use the REST API for creating new connectors in the following section.


// 7. Kafka Connect in Distributed Mode

// The standalone mode works perfectly for development and testing,
// as well as smaller setups.
// However, if we want to make full use of the distributed nature of Kafka,
// we have to launch Connect in distributed mode.

// By doing soe, connector settings and metadata sre stored in Kafka topics
// intead of the file system.
// As a result, the worker nodes are really stateless.


// 7.1. Starting Connect

// A reference configuration for distributed mode can be found at 
// $CONFLUENT_HOME/etc/kafka/connect-distributed.properties.

// Parameters are mostly the same as for standalone mode.
// There are only a few differences:

// * group.id defines the name of the Connect cluster group.
// The value must be different from any consumer group ID
// * offset.storage.topic,  config.storage.topic and status.storage.topic
// define topics for these settings.
// For each topic, we can also define a replication factor

// Again, the official documentation provides a list with all parameters. // http://kafka.apache.org/documentation/#connectconfigs

// We can start Connect in distributed mode as follows:

$CONFLUENT_HOME/bin/connect-distributed $CONFLUENT_HOME/etc/kafka/connect-distributed.properties


// 7.2. Adding Connectors Using the REST API

// Now, compared to the standalone startup command, we didn't pass any connector configuration
// as arguments.
// Instead, we have to create the connectors using the REST API.

// To set up our example from before, we have to send two POST requests
// to http://localhost:8083/connectors
// containing the following JSON structs.

// First, we need to create the body for the source connector POST as a JSON file.
// Here, we'llcall it connect-file-source.json

{
    "name": "local-gile-source",
    "config": {
        "connector.class": "FileStreamSource",
        "tasks.max": 1,
        "file": "test-distributed.txt",
        "topic": "connect-distributed"
    }
}

// Note how this looks pretty similar to the reference configuration
// file we used the first time.

// And then we POST it:

curl -d @"$CONFLUENT_HOME/connect-file-source.json" \
    -H "Content-Type: application\json" \
    -X POST http://localhost:8083/connectors

// Then, we'll do the same for the sink connector,
// calling the file context-file-sink.json:

{
    "name": "local-file-sink",
    "config": {
        "connector.class": "FileStreamSink",
        "tasks.max": 1,
        "file": "test-distributed.sink.txt",
        "topics": "connect-distributed"
    }    
}

// And perform the POST like before:

curl -d @$CONFLUEN_HOME/connect-file-sink.json \
    -H "Content-Type: application/json" \
    -X POST http://localhost:8083/connectors

// If needed, we can verify, that this setup is working correctly:

$CONFLUENT_HOME/bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic connect-distributed-from-beginning
{"schema":{"type":"string","optional":false},"payload":"foo"}
{"schema":{"type":"string","optional":false},"payload":"bar"}

// And, if we have a look at the folder $CONFLUENT_HOME,
// we can see that a file test-distributed.sink.txt was created here:

cat $CONFLUENT_HOME/test-distributed.sink.txt
foo
bar

// After we tested the distributed setup,
// let's clean up by removing the two connectors:

curl -X DELETE http://localhost:8083/connectors/local-file-source
curl -X DELETE http://localhost:8083/connectors/local-file-sink


// 8. Transforming Data

// 8.1. Supported Transformations

// Transformations enable us to make simple
// and lightweight modifications to individual messages

// Kafka Connect supports the following built-in tranformations:
// * InsertField - Add a field using either static data or record metadata
// * ReplaceField - Filter or rename fields
// * MaskField - Replace a field with the valid null value for the type (zero or empty string, for example)
// * HoistField - Wrap the entire event as a single field inside a struct or a map
// * ExtractField - Extract a specific field from struct and map and include only this field in the results
// * SetSchemaMetadata - Modify the schema name or version
// * TimestampRouter - Modify the topic of a record based on original topic and timestamp
// * RegexRouter - Modify the topic of a record based on original topic, a replacement string, and a regular expression

// A transformation is configured using the following parameters:
// * transform - A comma-separated list of aliases for the transformations
// * transforms.$alias.type - Class name for the tranformation
// * tranfroms.$alias.$transformationSpecificConfig - Configuration for the respective transformation


// 8.2. Applying a Transformer

// To test some transformation features,
// let's split up the following two tranformations:
// * First, let's wrap the entire message as a JSON struct
// * After that, let's add afield to that struct

// Before applying our transformations, 
// we have to configure Connect to use schemaless JSON,
// by modifying the connect-distributed.properties:

key.converter.schemas.enable=false
value.converter.schemas.enable=false

// After that, we have to restart Connect, again in distributed mode:

$CONFLUENT_HOME/bin/connect-distributed $CONFLUENT_HOME/etc/kafka/connect-distributed.properties






