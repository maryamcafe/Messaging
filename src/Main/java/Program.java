import Broker.MessageBroker;
import Consumer.ConsumerGroup;
import Producer.ProducerGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Program {

    private String[] args;
    private MessageBroker messageBroker;
    static final Logger logger = LogManager.getLogger(Program.class);
    private int numberOfProducerGroups = 2;

    Program(String[] args) {
        this.args = args;
        messageBroker = new MessageBroker();
    }

    private File getProducerGroupDirectory() {
        if (args.length > 0) {
            return new File(args[0]);
        } else {
            return new File("data/");
        }
    }

    void run() {
        int numberOfConsumers = 10; // should be read from a config file

//        List<ProducerGroup> producerGroupList = new ArrayList<>(2);
//        List<ConsumerGroup> consumerGroupList = new ArrayList<>(2);
//        if (args.length > 0) {
//            for (String path : args) {
//                String topicName = getProducerGroupDirectory(path).getName();
//                producerGroupList.add(new ProducerGroup(messageBroker, getProducerGroupDirectory(), topicName));
//                consumerGroupList.add(new ConsumerGroup(messageBroker, topicName, topicName + "Readers", new File(topicName + ".txt"), numberOfConsumers));
//            }
//        } else {
//            File producerGroupDirectory = new File("data/");
//            producerGroupList.add(new ProducerGroup(messageBroker, producerGroupDirectory, topicName))
//        }
        File producerGroupDirectory = getProducerGroupDirectory();
        String topicName = producerGroupDirectory.getName();
        logger.info("topic name for the first producerGroup: " + topicName);

        File consumerGroupFile = new File(topicName + ".txt");
        String consumerGroupName = topicName + "Readers";

        ProducerGroup producerGroup = new ProducerGroup(messageBroker, producerGroupDirectory, topicName);
        ConsumerGroup consumerGroup = new ConsumerGroup(messageBroker, topicName, consumerGroupName, consumerGroupFile, numberOfConsumers);



        producerGroup.start();
        logger.info("First producer group started.");
//        consumerGroup.start();
//        logger.info("First consumer group started.\n");

        while (producerGroup.isAlive() || consumerGroup.isAlive()) {
            try {
                producerGroup.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("program finished");
    }

    private Path getProducerGroupDirectory(String path) {
        return null;
    }
}

