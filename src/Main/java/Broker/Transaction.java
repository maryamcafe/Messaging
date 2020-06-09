package Broker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Queue;

public class Transaction {

    private final TopicWriter topicWriter;
    private String producerName;
    private final Queue<Integer> values;

    static final Logger logger = LogManager.getLogger(Transaction.class);

    Transaction(TopicWriter topicWriter, String producerName) {
        this.topicWriter = topicWriter;
        this.producerName = producerName;
        values = new LinkedList<>();
    }

    void put(int value) {
        values.add(value);
    }

    void commit() {
        synchronized (topicWriter) {
//            logger.debug(0 + " is being sent to TopicWriter.write");
            topicWriter.writeValue(0);
            while (!values.isEmpty()) {
                int value = values.remove();
//                logger.debug(value + " is being sent to TopicWriter.write");
                topicWriter.writeValue(value);
            }
//            logger.debug(-1 + " is being sent to TopicWriter.write");
            topicWriter.writeValue(-1);
        }
    }


}
