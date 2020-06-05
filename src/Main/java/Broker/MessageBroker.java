package Broker;

import Producer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MessageBroker {
    private Map<String, Topic> topics = new HashMap<>();
    static final Logger logger = LogManager.getLogger(MessageBroker.class);

    private void addTopic(String name) {
        logger.info(String.format("Topic \"%s\" added in the MessageBroker.", name));
        topics.put(name, new Topic(name));
    }

    public synchronized void put(String topic, String producerName, int value) {
        if(!topics.containsKey(topic)) {
            addTopic(topic);
        }
        topics.get(topic).put(producerName, value);
//        logger.info(String.format("Value %d was put by producer \"%s\" in the MessageBroker.", value, producerName));
    }

    public int getValue(String topic, String groupName, String consumerName) throws NoSuchTopicException, IOException {
        if(!topics.containsKey(topic)) {
//            throw new NoSuchTopicException(topic);
            return 0;
        }
        return topics.get(topic).getValue(groupName,consumerName);
    }
}
