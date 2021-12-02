package homework;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CustomerService {

    private final NavigableMap<Customer, String> customers;

    public CustomerService() {
        customers = new TreeMap<>();
    }

    /**
     * тесты меняют состояние объекта !(в нашем случае ..и не откатывают состояние Map)
     * поэтому надо возвращать копию объекта...
     */

    public Map.Entry<Customer, String> getSmallest() {
        return copyCustomersMap(customers).firstEntry();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {// ближайшее в Map по отношению к подаваемому (по score)
        return copyCustomersMap(customers).higherEntry(customer);
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }

    /**
     * В Java нет глубокого копирования для HashMap
     */
    public NavigableMap<Customer, String> copyCustomersMap(NavigableMap<Customer, String> map) {
        NavigableMap<Customer, String> customersCopy = new TreeMap<>();
        map.forEach((key, value) -> customersCopy.put(new Customer(key), value));
        return customersCopy;
    }
}

