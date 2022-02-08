package ru.otus.dao;

import ru.otus.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    User saveUser(User user);
    Optional<User> findById(long id);
    List<User> findAll();
    Optional<User> findByLogin(String login);
}
