# 2025 03 17

function create_topic {
    topic_name="$1"
    bin/kafka-topics.sh --create --topic ${topic_name} --if-not-exists \
      --partitions 1 --replication-factor 1 \
      --zookeeper localhost:2181
}


