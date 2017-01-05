package dk.dbc.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dbc.kafka.logformat.LogEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by andreas on 12/8/16. Inspired by javaworld 'Big data messaging with Kafka, Part 1'
 */
public class Consumer {

    private static Logger LOGGER = Logger.getLogger("Consumer");
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSZ").create();
    private Date start, end;
    private String appID, host, env;


    /**
     * Consume kafka topics
     *
     * @param hostname
     * @param port
     * @param topicName
     * @param groupId
     * @param offset             The consumer can starts from the beginning of the topic or the end ["latest", "earliest"]
     * @param clientID           identify the consumer
     * @param maxNumberOfRecords
     * @return
     */
    public boolean readKafkaTopics(String hostname, String port, String topicName, String groupId, String offset, String clientID, int maxNumberOfRecords) {

        // setup consumer
        Properties consumerProps = createKafkaConsumerProperties(hostname, port, groupId, offset, clientID);


        KafkaConsumer<Integer, byte[]> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Arrays.asList(topicName));

        // starting consumer
        ConsumerRecords<Integer, byte[]> records = consumer.poll(3000);
        Iterator<ConsumerRecord<Integer, byte[]>> recordIterator = records.iterator();
        if (records.count() == 0) {
            LOGGER.warning("No records found in topic");
            return false;
        } else {
            LOGGER.info("Topic: " + topicName + " Count: " + records.count());
            while (recordIterator.hasNext()) {
                ConsumerRecord<Integer, byte[]> record = recordIterator.next();
                System.out.println(new String(record.value(), StandardCharsets.UTF_8));
            }
        }

        return true;
    }

    public void setRelevantPeriod(Date start, Date end) {
        this.start = start;
        this.end = end;
    }


    /**
     * Consume kafka topics
     *
     * @param hostname
     * @param port
     * @param topicName
     * @param groupId
     * @param offset             The consumer can starts from the beginning of the topic or the end ["latest", "earliest"]
     * @param clientID           identify the consumer
     * @param maxNumberOfRecords
     * @return
     */
    public List<LogEvent> readLogEventsFromTopic(String hostname, String port, String topicName, String groupId, String offset, String clientID, int maxNumberOfRecords) {

        // setup consumer
        Properties consumerProps = createKafkaConsumerProperties(hostname, port, groupId, offset, clientID);


        KafkaConsumer<Integer, byte[]> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Arrays.asList(topicName));
        List<LogEvent> output = new ArrayList<LogEvent>();

        // starting consumer
        ConsumerRecords<Integer, byte[]> records = consumer.poll(3000);
        Iterator<ConsumerRecord<Integer, byte[]>> recordIterator = records.iterator();
        if (records.count() == 0) {
            LOGGER.warning("No records found in topic");
            return output;
        } else {
            LOGGER.info("Topic: " + topicName + " Count: " + records.count());
            LogEvent logEvent = null;
            boolean relevant;

            while (recordIterator.hasNext()) {
                ConsumerRecord<Integer, byte[]> record = recordIterator.next();
                logEvent = gson.fromJson(new String(record.value(), StandardCharsets.UTF_8), LogEvent.class);
                relevant = true;
                //  filter events!
                if (this.start != null && logEvent.getTimestamp().before(this.start) &&
                        this.end != null && logEvent.getTimestamp().after(this.end)) {
                    relevant = false;
                }

                if (this.appID != null && !this.appID.isEmpty() && !logEvent.getAppID().equalsIgnoreCase(this.appID)) {
                    relevant = false;
                }

                if (this.env != null && !this.env.isEmpty() && !logEvent.getEnv().equalsIgnoreCase(this.env)) {
                    relevant = false;
                }

                if (this.host != null && !this.host.isEmpty() && !logEvent.getHost().equalsIgnoreCase(this.host)) {
                    relevant = false;
                }

                if (relevant) {
                    System.out.println(logEvent);
                    output.add(logEvent);
                }
            }
        }

        return output;
    }

    private Properties createKafkaConsumerProperties(String hostname, String port, String groupId, String offset, String clientID) {
        Properties consumerProps = new Properties();
        consumerProps.setProperty("bootstrap.servers", hostname + ":" + port);
        consumerProps.setProperty("group.id", groupId);
        consumerProps.setProperty("client.id", clientID); // UUID.randomUUID().toString()
        consumerProps.setProperty("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        consumerProps.setProperty("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        //consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // TODO set a max number of records to consume or infinite.
        consumerProps.put("auto.offset.reset", offset);  // The consumer can starts from the beginning of the topic or the end
        return consumerProps;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}