package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@NamedEntityGraph(name = "client-entity-graph",
//                   attributeNodes = {@NamedAttributeNode("address"), @NamedAttributeNode("phones")})
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private List<Phone> phones;


    public Client(String name) {
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public Client clone() {
        Address address = null;
        if(this.address != null) address = new Address(this.address.getId(), this.address.getStreet());
        List<Phone> phones = null;
        if(this.phones != null) phones = this.phones.stream().map(phone -> new Phone(phone.getId(), phone.getNumber())).collect(Collectors.toList());
        return new Client(this.id, this.name, address, phones);
    }

    @Override
    public String toString() {
        return "Client{}";
    }
}
