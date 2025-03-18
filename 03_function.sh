# 2025 03 17

function create_topic {
    topic_name="$1"
    bin/kafka-topics.sh --create --topic ${topic_name} --if-not-exists \
      --partitions 1 --replication-factor 1 \
      --zookeeper localhost:2181
}

# Next, let's create two standalone scripts, create-topic.sh and get-topic-retention-time.sh:
bash-5.1.# cat create-topic.sh
#!/bin/bash
. ./functions.sh
topic_name="$1"
create_topic "${topic_name}"
exit $?

bash-5.1# cat get-topic-retention-time.sh
#!/bin/baash
. ./functions.sh
topic_name="$1"
decsribe_topic_config "${topic_name}" | awk 'BEGIN{IFS="=";IRS=" "} /^[ ]*retention.ms/{print $1}'
exit $?

# We must note that describe_topic_config fill give all the properties required for the topic.
# So, we used the awk one-liner to add a filter for the retention.ms property. -- https://www.baeldung.com/linux/awk-guide # AWK (/ɔːk/[4]) is a domain-specific language designed for text processing and typically used as a data extraction and reporting tool. Like seq and grep, it is a filter, and it is a standard feature of most Unit-like operating systems. a set of actions to be taken against streams of textual data # sed ("stream editor") # grep is a command-line utility for searching plaintext datasets for lines that match a regular expression.

# Finally, let's start the Kafka environment # https://kafka.apache.org/documentation/#quickstart_startserver
# and verify retention period configuration for a new sample topic:
bash-5.1# ./create-topic.sh test-topic
Created topic test-topic.
bash-5.1# ./get-topic-retention-time.sh test-topic
retention.ms=600000

# Once the topic is created and described,
# we'll notice that retention.ms is set to 600000 (ten minutes).
# That's actually derived from the log.retention.minutes property
# that we had earlier defined in the server.properties file.


# 4. Topic-Level Configuration

# Once the Broker server is started, log.retention.{hours|minutes|ms} server-level properties becomes read-only.

# On the other hand, we get access to the retention.ms property, which we can tune at the topic-level.

# Let's add a method in our functions.sh script to configure a property of a topic:

funcion alter_topic_config {
  topic_name="$1"
  config_name="$2"
  config_value="$3"
  ./bin/kafka-configs.sh --alter \
    --add-config ${config_name}=${config_value} \
    --bootstrap-server=0.0.0.0:9092 \
    --topic ${topic_name}
}



