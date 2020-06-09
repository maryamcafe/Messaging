package Consumer;

import Broker.MessageBroker;
import Broker.NoSuchTopicException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Consumer extends Thread {

    private final ConsumerGroup consumerGroup;
    private final String consumerName, consumerGroupName;

    static final Logger logger = LogManager.getLogger(Consumer.class);

    Consumer(ConsumerGroup consumerGroup, String consumerName) {
        this.consumerGroup = consumerGroup;
        this.consumerName = consumerName;
        consumerGroupName = consumerGroup.getGroupName();
        setName(consumerName);
    }

    public void run() {
        while (true) {
            try {
                consumerGroup.performAction(this, getValue());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public int getValue() throws Exception {
        return getMessageBroker().getValue(getTopicName(), consumerGroupName, consumerName);
    }

    public MessageBroker getMessageBroker() {
        return consumerGroup.getMessageBroker();
    }

    public String getConsumerName() {
        return consumerName;
    }

    public String getTopicName() {
        return consumerGroup.getTopicName();
    }
}
