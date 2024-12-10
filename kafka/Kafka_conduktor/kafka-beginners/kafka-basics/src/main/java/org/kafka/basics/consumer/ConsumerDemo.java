package org.kafka.basics.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConsumerDemo {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class);
    public static void main(String[] args) {
        log.info("Consumer Demo Started");

        String groupId = "my-java-application";
        String topic = "demo_java";

        // Create A producer properties
        Properties properties = new Properties();

        // Connect to localhost
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");

        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);

        // below property has 3 values = earliest, latest, and none
        // earliest - starts from the oldest available record
        // latest - starts from the newest available record
        // none - throws exception if consumer group is not set at the start of application
        properties.setProperty("auto.offset.reset", "earliest");

        try{
            // Create a new Kafka producer instance
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

            // Subscribe to a topic
            consumer.subscribe(List.of(topic));

            // poll for data
            while (true) {
                log.info("Polling");
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records){
                    log.info("Received: Key; {}, value: {}", record.key(), record.value());
                    log.info("Partition: {}, offset: {}", record.partition(), record.offset());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
