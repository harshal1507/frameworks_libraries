package org.kafka.basics.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class ConsumerDemoWithShutdown {

    // As soon as the consumer is shutdown the WakeupException will be thrown by poll method
    // then we will wake up the consumer and join it with main thread
    // to finish the polling of pending offsets/messages and then gracefully shutdown

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoWithShutdown.class);
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


        // Create a new Kafka producer instance
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // get a reference to the main thread
        final Thread mainThread = Thread.currentThread();

        // add the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Detected shutdown, let's exit by calling consumer.wakeup() method");
            consumer.wakeup();

            // join thr main thread to allow the execution of the code in the main thread
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                log.info("Exception while joining main thread");
            }

        }));

        try{
            // Subscribe to a topic
            consumer.subscribe(List.of(topic));

            // poll for data
            while (true) {
//                log.info("Polling");
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records){
                    log.info("Received: Key; {}, value: {}", record.key(), record.value());
                    log.info("Partition: {}, offset: {}", record.partition(), record.offset());
                }
            }

        } catch (WakeupException e) {
            log.error("Consumer is starting to shutdown");
        } catch (Exception e) {
            log.error("Unexpected exception in the consumer", e);
        }finally{
            consumer.close(); // close the consumer, this will also commit offsets
            log.info("Consumer is now gracefully shut down");
        }
    }
}
