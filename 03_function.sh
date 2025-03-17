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

