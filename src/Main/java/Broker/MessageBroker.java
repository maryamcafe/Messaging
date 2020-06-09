package Broker;

import Producer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MessageBroker {
    private Map<String, Topic> topics = new HashMap<>();
    static final Logger logger = LogManager.getLogger(MessageBroker.class);

    public synchronized void put(String topic, String producerName, int value) {
        if(!topics.containsKey(topic)) {
            addTopic(topic);
        }
        topics.get(topic).put(producerName, value);
//        logger.info("Put value =  " + value);
    }

    public int getValue(String topic, String consumerGroupName, String consumerName) throws Exception {
        if(!topics.containsKey(topic)) {
            throw new NoSuchTopicException(topic);
        }
        return topics.get(topic).getValue(consumerGroupName,consumerName);
    }

    private void addTopic(String name) {
        logger.info(String.format("Topic \"%s\" added in the MessageBroker.", name));
        topics.put(name, new Topic(name));
    }

}
