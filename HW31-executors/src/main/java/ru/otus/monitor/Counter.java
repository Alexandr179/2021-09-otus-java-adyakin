package ru.otus.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter {
    private static final Logger logger = LoggerFactory.getLogger(Counter.class);
    private Integer last = 2;


    private synchronized void action(Integer threadNum) {
        int count = 0;
        boolean reverse = false;
        while(!Thread.currentThread().isInterrupted()) {
            try {
                //spurious wakeup https://en.wikipedia.org/wiki/Spurious_wakeup
                //поэтому не if
                while (last.equals(threadNum)) {
                    this.wait();
                }

                if(!reverse) {
                    if(count < 10){
                        count ++;
                    } else {
                        count --;
                        reverse = true;
                    }
                } else {
                    if(count > 0){
                        count --;
                    } else Thread.currentThread().interrupt();
                }
                
                logger.info("{}",count);
                last = threadNum;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Counter count = new Counter();
        new Thread(() -> count.action(1)).start();
        new Thread(() -> count.action(2)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
