package ru.otus.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.User;
import ru.otus.repository.DataTemplate;
import ru.otus.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    public UserDaoImpl(TransactionManager transactionManager, DataTemplate<User> userDataTemplate) {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
    }

    @Override
    public User saveUser(User user) {
        return transactionManager.doInTransaction(session -> {
            var userCloned = user.clone();
            if (user.getId() == null) {
                userDataTemplate.insert(session, userCloned);
                log.info("created user: {}", userCloned);
                return userCloned;
            }
            userDataTemplate.update(session, userCloned);
            log.info("updated user: {}", userCloned);
            return userCloned;
        });
    }

    @Override
    public Optional<User> findById(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var userOptional = userDataTemplate.findById(session, id);
            log.info("user: {}", userOptional);
            return userOptional;
        });
    }

    @Override
    public List<User> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var userList = userDataTemplate.findAll(session);
            log.info("userList:{}", userList);
            return userList;
       });
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            List<User> users = userDataTemplate.findByEntityField(session, "login", login);
            log.info("user: {}", users);
            return users.stream().filter(v -> v.getLogin().equals(login)).findFirst();
        });
    }
}
