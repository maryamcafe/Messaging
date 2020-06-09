package Consumer;

import Broker.MessageBroker;
import Broker.TopicReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

public class ConsumerGroup extends Thread {
    private ArrayList<Consumer> consumers;
    private final MessageBroker messageBroker;
    private final String topicName, groupName;
    private final int numberOfConsumers;

    private final File consumerGroupFile;
    private PrintWriter printWriter;
    private FileWriter fileWriter;

    static final Logger logger = LogManager.getLogger(ConsumerGroup.class);

    public ConsumerGroup(MessageBroker messageBroker, String topicName, String groupName, File consumerGroupFile, int numberOfConsumers) {
        this.messageBroker = messageBroker;
        this.consumerGroupFile = consumerGroupFile;
        this.topicName = topicName;
        this.groupName = groupName;
        this.numberOfConsumers = numberOfConsumers;
        setName(groupName);
        consumers = new ArrayList<>();
    }

    private void initialize() throws IOException {
        for (int i = 0; i < numberOfConsumers; i++) {
            String consumerName = groupName + "_" + i;
            consumers.add(new Consumer(this, consumerName));
        }
        logger.trace(numberOfConsumers + " consumers added.");
        fileWriter = new FileWriter(consumerGroupFile);
//        fileWriter.write("Salaam!\n"); //just testing

    }

    public void run() {
        try {
            initialize();
            for (Consumer consumer : consumers) {
                consumer.start();
            }
        } catch (FileNotFoundException e) {
            logger.error("ConsumerGroup File not found.", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void performAction(Consumer consumer, int value) {
        try {
            fileWriter.write("Consumer with name " + consumer.getConsumerName() + " read the value " + value + "\n");
            fileWriter.flush();
            logger.debug("Flushed:" + value);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public String getTopicName() {
        return topicName;
    }

    public MessageBroker getMessageBroker() {
        return messageBroker;
    }

    public FileWriter getPrintWriter() {
        return fileWriter;
    }
}

