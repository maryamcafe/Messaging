"# Messaging" 

##Changes made to the raw project

Log4j dependency was added to the project, it's format and appenders were configured do that it would print logs both on the console and a .log file plus a separate .log file for each program run.
Logs were added to the project, from Program.java and ProducerGroup.java all the way to TopicWriter.java. Let's name this part of the project "producer side", which takes care of writing all the messages from a topic to the topicFile.
The producer side's code was completed and synchronized blocks on the topicWriter object were added to places where something was going to be written of the file.

Class MyWaitNotify was added; each time method "doNotify" is called on an object of it, it would be count and "consumed" later when doWait is called. It's good to mention that these two methods are implemented synchronized themselves, so there's no need to use them in a synchronized block.

Also TopicReader.java was made complete by adding a mechanism to wait, while there's no message, and also when it's not a consumer's turn requesting through the TopicReader.(when a consumer starts a transaction, it would be it's turn and others should wait). This was achieved using two objects as keys for waiting and notifying: one is A MyWaitNotify object and the other is the monitor object inside MyWaitNotify (which we don't see but is used in doWait and doWait Methods). Not to mention that we also previously used the TopicWriter object as a key to sync writing to topicFile mechanism in this class.
Finally the data given to the consumers is written through ConsumerGroup.java on "data_reader.txt", in the exact order as it is in the topicFile ("data.dat" in this case), but the data is from different consumers.
