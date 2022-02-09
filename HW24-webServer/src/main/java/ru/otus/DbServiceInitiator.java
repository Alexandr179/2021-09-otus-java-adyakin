package ru.otus;

import org.hibernate.cfg.Configuration;
import ru.otus.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.model.User;
import ru.otus.repository.DataTemplateHibernate;
import ru.otus.repository.HibernateUtils;
import ru.otus.dao.UserDao;
import ru.otus.dao.UserDaoImpl;
import ru.otus.sessionmanager.TransactionManagerHibernate;

public class DbServiceInitiator {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static UserDao getServiceUser() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, User.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(User.class);

        UserDaoImpl userDao = new UserDaoImpl(transactionManager, clientTemplate);
        User user = new User("Кто мы.. Super_legacy_Production представляет", "user7", "11111");
        User user2 = new User("Сервлеты вытеснили все. Мир уже никогда не будет таким, как прежде", "user", "11111");
        userDao.saveUser(user);
        userDao.saveUser(user2);
        return userDao;
    }
}
