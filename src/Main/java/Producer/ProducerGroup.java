package Producer;

import Broker.MessageBroker;
import Broker.TopicWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ProducerGroup extends Thread {
    private ArrayList<Producer> producers;
    private File producerGroupDirectory;
    private MessageBroker messageBroker;
    private String topicName;
    static final Logger logger = LogManager.getLogger(ProducerGroup.class);


    public ProducerGroup(MessageBroker messageBroker, File producerGroupDirectory, String topicName) {
        this.messageBroker = messageBroker;
        this.producerGroupDirectory = producerGroupDirectory;
        this.topicName = topicName;
        setName(topicName);
        producers = new ArrayList<>();
    }

    private void initialize() {
        try {for(File file: Objects.requireNonNull(producerGroupDirectory.listFiles())) {
            producers.add(new Producer(messageBroker,topicName, file.getName(), file));
        }}catch (NullPointerException e){
            logger.error("No file was found in producer group directory", e);
            e.printStackTrace();
        }
    }

    public void run() {
        logger.trace("Started to run.");
        initialize();

        for(Producer producer: producers) {
            producer.start();
        }
    }
}
