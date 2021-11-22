package homework;


import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class CustomerService {

    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    public SortedMap<Customer, String> map = new TreeMap<>();


    public Map.Entry<Customer, String> getSmallest() {
        Customer customer = map.firstKey();// smallest Customer
        String string = map.get(customer);
        return Map.entry(customer, string);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return map.entrySet().stream()
                .filter(currentEntry -> currentEntry.getKey().getName().equals(customer.getName()))
                .findFirst()
                .orElse(null);
    }

// todo: УСЛОВИЕ ..  a key-value mapping associated with the least key strictly greater than the given key, or null if there is no such key
//                                  означает? ->
// todo:        поиск в стриме максимально приближенного (по score) значения к подаваемому на вход <Customer customer> с идентечным именем (Name)

//    public Map.Entry<Customer, String> getNext(Customer customer) {
//        return map.entrySet().stream()
//                .reduce((left, right) ->
//                        (Math.abs(left.getKey().getScores() - customer.getScores()) < Math.abs(right.getKey().getScores() - customer.getScores()))
//                                ? left : right)
//                .filter(currentEntry -> currentEntry.getKey().getName().equals(customer.getName()))
//                //&& currentEntry.getKey().getScores    () > customer.getScores()
//                .orElse(null);
//    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
