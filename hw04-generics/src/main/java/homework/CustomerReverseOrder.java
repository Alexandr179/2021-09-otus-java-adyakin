package homework;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    public Queue<Customer> customerQueue;

    public CustomerReverseOrder() {
        if(customerQueue == null) customerQueue = new ArrayDeque<>();
//        customerQueue = new ArrayDeque<>();
    }

    public void add(Customer customer) {
        customerQueue.offer(customer);
    }

    public Customer take() {
        return customerQueue.poll(); // это "заглушка, чтобы скомилировать"
    }
}
