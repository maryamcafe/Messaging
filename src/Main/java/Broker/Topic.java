package Broker;

import Producer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.MyWaitNotify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Topic {
    private final String name;

    private File topicFile;
    private final TopicWriter topicWriter;
    private final HashMap<String, TopicReader> topicReaders;
    private static final Logger logger = LogManager.getLogger(Topic.class);
    private final MyWaitNotify notifier;

    Topic(String name) {
        this.name = name;
        initFile();
        notifier = new MyWaitNotify();
        topicWriter = new TopicWriter(this, notifier);
        topicReaders = new HashMap<>();
    }

    private void initFile() {
        Path path = Paths.get(name + ".dat");
        topicFile = path.toFile();
        try {
            Files.createFile(path);
        } catch (IOException e) {
            logger.warn("Topic file already existed: " + e.getMessage());
        }
    }

    /**
     * This method is used to get the first value in the topic file which is not read in the given group yet,
     * and serve it for the appropriate consumer.
     *
     * @return the value of the first remained item.
     */
    public int getValue(String consumerGroupName, String consumerName) throws Exception {
        if (!topicReaders.containsKey(consumerGroupName)) {
            addGroup(consumerGroupName);
        }
        return topicReaders.get(consumerGroupName).getValue(consumerName);
    }

    private synchronized void addGroup(String consumerGroupName) {
        logger.debug("readers include " + consumerGroupName + "? " + topicReaders.containsKey(consumerGroupName));
        if (!topicReaders.containsKey(consumerGroupName)) {
            topicReaders.put(consumerGroupName, new TopicReader(this, consumerGroupName, notifier));
            logger.debug("new consumer group is being added: " + consumerGroupName);
        }
    }

    /**
     * This method is used to put the given value at the tail of the topic file.
     *
     * @param value the value to be put at the end of the topic file
     * @return Nothing.
     */
    public void put(String producerName, int value) {
        topicWriter.put(producerName, value);
    }


    public File getTopicFile() {
        return topicFile;
    }

}
