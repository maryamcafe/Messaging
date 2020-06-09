package Broker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.MyWaitNotify;

import java.io.IOException;
import java.io.RandomAccessFile;

public class TopicReader {

    RandomAccessFile topicFile;

    private Topic topic;
    private String groupName;
    private String transactionHolder;
    private final MyWaitNotify messageNotifier;

    private static final Logger logger = LogManager.getLogger(TopicReader.class);

    TopicReader(Topic topic, String groupName, MyWaitNotify notifier) {
        this.topic = topic;
        this.groupName = groupName;
        try {
            topicFile = new RandomAccessFile(topic.getTopicFile(), "r");
//            topicFile.seek(0); // redundant
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        messageNotifier = notifier;
        transactionHolder = null;
        logger.trace("TopicReader initialized.");
    }

    //To Do - Handle transaction constraints.
    public int getValue(String consumerName) throws IOException {
        synchronized (messageNotifier) {
            if (consumerName.equals(transactionHolder) || transactionHolder == null) {
                messageNotifier.doWait();
                logger.debug("topic file pointer is on " + topicFile.getFilePointer() + " while it's length is " + topicFile.length());
                logger.debug("Transaction holder is :" + transactionHolder);

                int value = topicFile.readInt();
                logger.debug("The read value is = " + value);

                if (value == 0) {
                    startTransaction(consumerName);
                } else if (value == -1) {
                    finishTransactions(consumerName);
                } else if (value < -1) {
                    logger.error("Wrong message in the topicFile.");
                }
                return value;
            } else {
                waitForYourTurn(); // waits until it's notified by the transaction thread
                return getValue(consumerName);
            }
        }
    }

    private void waitForYourTurn() {
//        synchronized (messageNotifier) {
            try {
                logger.debug("I'm going to wait");
                messageNotifier.wait();
            } catch (InterruptedException e) {
                logger.error("Consumer waiting was interrupted.");
                e.printStackTrace();
            }
//        }
    }

    private void startTransaction(String consumerName) {
        if (transactionHolder != null && transactionHolder.equals(consumerName)) {
            logger.warn("Previous transaction isn't closed ");
        } else {
            transactionHolder = consumerName;
            logger.debug("Transaction Holder is HERE.");
        }
    }

    private void finishTransactions(String consumerName) {
//        synchronized (messageNotifier) {
            if (transactionHolder != null && !transactionHolder.equals(consumerName)) {
                logger.warn("Trying to close a non-relevant transaction.");
            } else {
                logger.debug("I notified everybody");
                messageNotifier.notifyAll();
                transactionHolder = null;
                logger.debug("Transaction closed.");
            }
//        }
    }

}
