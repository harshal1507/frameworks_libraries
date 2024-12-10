package org.kafka.basics.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {

    // same key goes to same partition

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class);
    public static void main(String[] args) {
        log.info("Producer demo with callback started");

        // Create A producer properties
        Properties properties = new Properties();

        // Connect to localhost
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        KafkaProducer<String, String> producer;
        try{
            // Create a new Kafka producer instance
            producer = new KafkaProducer<>(properties);

            for (int i = 1; i <= 2; i++) {
                for (int j = 0; j < 10; j++) {
                    String topic = "demo_java";
                    String key = "key_" + j;
                    String value = "hello world " + j;

                    // create a producer record
                    ProducerRecord<String,String> producerRecord = new ProducerRecord<>(topic, key, value);

                    // Send data -- asynchronous in nature
                    // Here producer is by default using StickyPartitioner for performance improvements
                    producer.send(producerRecord, new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception exception) {
                            // executes every time a record successfully sent or an exception is thrown
                            if (exception == null) {
                                log.info("Message sent to topic {}, key {}, partition {}, offset {}, timestamp {}",
                                        metadata.topic(), key, metadata.partition(), metadata.offset(), metadata.timestamp());
                            } else {
                                log.error("Error sending message", exception);
                            }
                        }
                    });
                }
                Thread.sleep(500);
            }

            // tell the producer to send the data and block until done -- synchronous
            producer.flush();

            // Flush and close the producer -- close internally calls flush method
            producer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Producer demo with callback finished");
    }
}
