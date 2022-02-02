package ru.otus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.repository.DataTemplate;
import ru.otus.sessionmanager.TransactionManager;
import ru.otus.model.User;
import java.util.List;
import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger log = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceUserImpl(TransactionManager transactionManager, DataTemplate<User> userDataTemplate) {
        this.transactionManager = transactionManager;
        this.userDataTemplate = userDataTemplate;
    }

    @Override
//    @PrePersist
//    @PreUpdate
    public User saveUser(User user) {
        return transactionManager.doInTransaction(session -> {
            var userCloned = user.clone();
            if (user.getId() == null) {
                userDataTemplate.insert(session, userCloned);
                log.info("created user: {}", userCloned);
                return userCloned;
            }
            userDataTemplate.update(session, userCloned);
//            log.info("updated user: {}", userCloned);
            return userCloned;
        });
    }

    @Override
    public Optional<User> getUser(long id) {
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
}
