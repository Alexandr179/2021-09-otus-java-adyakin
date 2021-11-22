package homework;

import java.util.Objects;

public class Customer implements Comparable<Customer> {
    private final long id;
    private String name;
    private long scores;


    public Customer(long id, String name, long scores) {
        this.id = id;
        this.name = name;
        this.scores = scores;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getScores() {
        return scores;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScores(long scores) {
        this.scores = scores;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", scores=" + scores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return getId() == customer.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public int compareTo(Customer customer) {// https://stackoverflow.com/questions/1440006/java-sortedmap-treemap-comparable-how-to-use
        if(scores == customer.getScores()) return 0;
        if(scores > customer.getScores()) {
            return 1;
        } else {
            return -1;
        }
    }
}
