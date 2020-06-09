package util;

import Broker.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyWaitNotify {
    final private Object monitorObject;
    int notifNumber, maximumNotifs;

    static final Logger logger = LogManager.getLogger(MyWaitNotify.class);

    public MyWaitNotify() {
        this(new Object(), 0, 1);
    }

    public MyWaitNotify(Object monitorObject) {
        this(monitorObject, 0, 1);
    }

    public MyWaitNotify(Object monitorObject, int notifNumber) {
        this(monitorObject, notifNumber, 1);
    }

    public MyWaitNotify(Object monitorObject, int notifNumber, int maximumNotifs) {
        this.monitorObject = monitorObject;
        this.notifNumber = notifNumber;
        this.maximumNotifs = maximumNotifs;
    }

    public void doWait() {
        synchronized (monitorObject) {
            while (notifNumber <= 0) {
                try {
                    logger.debug("Im going to wait... my notif number is: " + notifNumber);
                    monitorObject.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notifNumber--;
            logger.debug("I used one of my \"notif\"s. it is now = " + notifNumber);
        }
    }

    public void doNotify() {
        synchronized (monitorObject) {
//            if(notifNumber < maximumNotifs)
            notifNumber++;
            monitorObject.notifyAll();
            logger.debug("I woke UP! my notifNumber is: " + notifNumber);
        }
    }

}
