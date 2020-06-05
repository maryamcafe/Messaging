package Broker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class TopicWriter {
    RandomAccessFile buffer;
    static final Logger logger = LogManager.getLogger(TopicWriter.class);

    //looks like this is the place where we write a topic to a topic.dat file.
    private Topic topic;
    private HashMap<String, Transaction> transactions;

    TopicWriter(Topic topic) {
        this.topic = topic;
        transactions = new HashMap<>();
        try {
            buffer = new RandomAccessFile(topic.getTopicFile(), "rws");
            buffer. seek(0);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        logger.trace("TopicWriter initiated");
    }

    public void put(String producerName, int value) {
        if (value <= 0) {
            handleTransactionOperation(producerName, value);
        } else {
            handleInsertOperation(producerName, value);
        }
    }

    private void handleTransactionOperation(String producerName, int value) {
        switch (value) {
            case 0:
                startTransaction(producerName);
                break;
            case -1:
                commitTransaction(producerName);
                break;
            case -2:
                cancelTransaction(producerName);
        }
    }

    private void handleInsertOperation(String producerName, int value) {
        if (transactions.containsKey(producerName)) {
            transactions.get(producerName).put(value);
            logger.info(String.format("Value %d was added to transaction.", value));
        } else {
            synchronized (this) {
                writeValue(value);
            }
        }
    }

    /**
     * This method is used to start a transaction for putting a transaction of values inside the buffer.
     *
     * @return Nothing.
     */
    private void startTransaction(String producerName) {
        //To Do - Log the problem in finalizing previous transaction.
        if (transactions.containsKey(producerName)) {
            logger.warn(String.format("Last transaction for producer \"%s\" was not closed.", producerName));
            commitTransaction(producerName);
            logger.info(String.format("Last transaction for producer \"%s\" committed automatically.", producerName));
            transactions.remove(producerName);
        }
        addTransaction(producerName);
    }

    private void addTransaction(String producerName) {
        transactions.put(producerName, new Transaction(this, producerName));
        logger.info("A new transaction added.");
    }

    /**
     * This method is used to end the transaction for putting a its values inside the file.
     *
     * @return Nothing.
     */
    private void commitTransaction(String producerName) {
        if (transactions.containsKey(producerName)) {
            transactions.get(producerName).commit();
            logger.info("Transaction committed.");
            transactions.remove(producerName);
        } else {
            //To Do - Log the problem in committing a non-existing transaction.
            logger.warn(String.format("Trying to commit a non-existing transaction from producer \"%s\".", producerName));
        }
    }

    /**
     * This method is used to cancel a transaction.
     *
     * @return Nothing.
     */
    private void cancelTransaction(String producerName) {
        if (transactions.containsKey(producerName)) {
            transactions.remove(producerName);
            logger.info(String.format("Transaction for producer \"%s\" was canceled.", producerName));
        } else {
            //To Do - Log the problem in canceling a non-existing transaction.
            logger.warn(String.format("Trying to cancel a non-existing transaction from producer \"%s\".", producerName));
        }
    }

    // to do - make sure a transaction is written sequential.
    public void writeValue(int value) {
        try {
            buffer.write(value);
            logger.info(String.format("Writing value = %d to the file.", value));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        //To Do - Put the given value at the end of the topicFile
    }
}
