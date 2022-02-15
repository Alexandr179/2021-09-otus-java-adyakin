package ru.otus.crm.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;


public interface ClientRepository extends CrudRepository<Client, Long> {

    Optional<Client> findById(Long id);

    List<Client> findAll();

    Optional<Client> findByName(String name);

}
