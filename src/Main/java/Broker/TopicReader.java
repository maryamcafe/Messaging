package Broker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TopicReader {

    RandomAccessFile topicFile;

    private Topic topic;
    private String groupName;

    TopicReader(Topic topic, String groupName) {
        this.topic = topic;
        this.groupName = groupName;
        try {
            topicFile = new RandomAccessFile(topic.getTopicFile(), "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //To Do - Generate topicFile
    }

    public int getValue(String consumerName) throws IOException {
        int value = 0;
        //To Do - Read next value from topicFile and return the value
        if (isTransaction()) {
            //the right person should wait
            //how to know isTransaction? is Transaction class helpful?
            return topicFile.read();
        } else {        //To Do - Handle the transaction constraints
            return value;
        }
    }

    private boolean isTransaction() {
        return false;
    }
}
