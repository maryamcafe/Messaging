package Consumer;

import Broker.MessageBroker;
import Broker.NoSuchTopicException;

import java.io.IOException;

public class Consumer extends Thread {

    private ConsumerGroup consumerGroup;
    private String consumerName;

    Consumer(ConsumerGroup consumerGroup, String consumerName) {
        this.consumerGroup = consumerGroup;
        this.consumerName = consumerName;
    }

    public int getValue() throws NoSuchTopicException, IOException {
        return getMessageBroker().getValue(getTopicName(), consumerGroup.getGroupName(), consumerName);
    }

    public void run() {
        while(true) {
            try {
                consumerGroup.performAction(this, getValue());
            } catch (NoSuchTopicException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public MessageBroker getMessageBroker() {
        return consumerGroup.getMessageBroker();
    }
    public String getConsumerName() {
        return consumerName;
    }
    public String getTopicName() { return consumerGroup.getTopicName();    }
}
