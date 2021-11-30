package homework;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> customers;

    public CustomerService() {
        customers = new TreeMap<>();
    }

    /**
     * тесты меняют состояние объекта !(в нашем случае ..и не откатывают состояние Map)
     * поэтому надо возвращать копию объекта.
     */

    public Map.Entry<Customer, String> getSmallest() {
        return new TreeMap<>(customers).firstEntry();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {// ближайшее в Map по отношению к подаваемому (по score)
        return new TreeMap<>(customers)
//                .entrySet().stream()
//                .filter(customerEntry -> customerEntry.getKey().getName().equals(customer.getName()))
                .higherEntry(customer);
    }

    public void add(Customer customer, String data) {
        customers.put(new Customer(customer), data);
    }
}
