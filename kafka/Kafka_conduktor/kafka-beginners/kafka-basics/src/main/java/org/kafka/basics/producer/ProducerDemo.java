package org.kafka.basics.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class);
    public static void main(String[] args) {
        log.info("Producer Demo Started");

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

        KafkaProducer<String, String> producer;
        try{
            // Create a new Kafka producer instance
            producer = new KafkaProducer<>(properties);

            // create a producer record
            ProducerRecord<String,String> producerRecord = new ProducerRecord<>("demo_java","hello world");

            // Send data -- asynchronous in nature
            producer.send(producerRecord);

            // tell the producer to send the data and block until done -- synchronous
            producer.flush();

            // Flush and close the producer -- close internally calls flush method
            producer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Producer Demo Finished");
    }
}
