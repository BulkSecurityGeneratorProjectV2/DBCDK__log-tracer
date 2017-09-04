package dk.dbc.kafka;

/**
 * Created by andreas on 12/8/16.
 */

import dk.dbc.kafka.logformat.LogEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;


public class ProduceTestData {
    private static int counter = 0;
    private static String [] environment = {"dev", "test", "stage", "prod"} ;
    private static String [] hostname_examples = {"mesos-node-1", "mesos-node-2", "mesos-node-3", "oldfaithfull"} ;
    private static String [] appid_examples = {"smooth-sink", "wild-webapp", "terrific-transformer", "dashing-database"} ;


    /**
     * Generate random test data
     * @param hostname kafkahost
     * @param port kafkaport
     * @param topicName name of the topic
     */
    public void produceTestData(String hostname, String port, String topicName ) {
        Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hostname + ":" + port);
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        org.apache.kafka.clients.producer.Producer producer = new KafkaProducer(configProperties);

        final Thread thread = new Thread(() -> {
            while(true) {
                try {
                    LogEvent logEvent = createDummyObject(counter++);
                    ProducerRecord<String, String> rec = new ProducerRecord<>(topicName, logEvent.toJSON());
                    System.out.println("Generating log event. " + logEvent.toJSON());
                    producer.send(rec);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    producer.close();
                    System.out.println("### Generated " + counter + " message(s)");
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static LogEvent createDummyObject(int id){
        LogEvent logEvent = new LogEvent();
        logEvent.setAppID(appid_examples[ThreadLocalRandom.current().nextInt(0, 4)]);
        logEvent.setHost(hostname_examples[ThreadLocalRandom.current().nextInt(0, 4)]);
        logEvent.setEnv(environment[ThreadLocalRandom.current().nextInt(0, 4)]);
        logEvent.setLevel(10* ThreadLocalRandom.current().nextInt(0, 5));
        logEvent.setMsg("This is an auto generated log message. Its number " + id);
        logEvent.setTimestamp(new Date());
        return logEvent;
    }


}