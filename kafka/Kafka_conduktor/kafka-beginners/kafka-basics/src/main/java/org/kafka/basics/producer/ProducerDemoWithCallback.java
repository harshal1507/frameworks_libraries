package org.kafka.basics.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallback.class);
    public static void main(String[] args) {
        log.info("Producer demo with callback started");

        // Create A producer properties
        Properties properties = new Properties();

        // Connect to localhost
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");

        // Connect to secured hosts
        /*
        properties.setProperty("bootstrap.servers","cluster.playground.cdkt.io:9092");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("sasl.mechanism","PLAIN");
        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"cdkt\" password=\"cdkt\";");
        */

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // both properties are not recommended in production
        // as kafka by default set very high batch size
        // Round Robin leads to performance issues in production as it is very heavy
        // here only used to check the sticky partitioner behavior
        properties.setProperty("batch.size", "400");
        properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());

        KafkaProducer<String, String> producer;
        try{
            // Create a new Kafka producer instance
            producer = new KafkaProducer<>(properties);

            for (int j = 0; j < 10; j++) {
                for (int i = 1; i <= 30; i++) {
                    // create a producer record
                    ProducerRecord<String,String> producerRecord = new ProducerRecord<>("demo_java","hello world " + i);

                    // Send data -- asynchronous in nature
                    // Here producer is by default using StickyPartitioner for performance improvements
                    producer.send(producerRecord, new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata metadata, Exception exception) {
                            // executes every time a record successfully sent or an exception is thrown
                            if (exception == null) {
                                log.info("Message sent to topic {}, partition {}, offset {}, timestamp {}",
                                        metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
                            } else {
                                log.error("Error sending message", exception);
                            }
                        }
                    });
                }
                Thread.sleep(5000);
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
