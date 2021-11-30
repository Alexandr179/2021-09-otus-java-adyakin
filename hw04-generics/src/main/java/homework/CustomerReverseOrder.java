package homework;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class CustomerReverseOrder {

    public Deque<Customer> customerQueue;

    public CustomerReverseOrder() {
        customerQueue = new ArrayDeque<>();
    }

    public void add(Customer customer) {
        customerQueue.push(customer);
    }

    public Customer take() {
        return customerQueue.pop();
    }
}
