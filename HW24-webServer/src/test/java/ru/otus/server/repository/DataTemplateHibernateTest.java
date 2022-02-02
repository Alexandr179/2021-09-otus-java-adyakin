package ru.otus.server.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.User;
import ru.otus.server.base.AbstractHibernateTest;

import static org.assertj.core.api.Assertions.assertThat;

class DataTemplateHibernateTest extends AbstractHibernateTest {

    @Test
    @DisplayName(" корректно сохраняет, изменяет и загружает клиента по заданному id")
    void shouldSaveAndFindCorrectClientById() {
        //given
        // var client = new Client("Вася");
        var user = new User(null, "Vasya", "VasinLogin", "VasinPassword");


        //when
        var savedUser = transactionManager.doInTransaction(session -> {
            userTemplate.insert(session, user);
            return user;
        });

        //then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(user.getName());

        //when
        var loadedSavedUser = transactionManager.doInReadOnlyTransaction(session ->
                userTemplate.findById(session, savedUser.getId())
        );

        //then
        assertThat(loadedSavedUser).isPresent().get().usingRecursiveComparison().isEqualTo(savedUser);

        //when
        var updatedUser = savedUser.clone();
        updatedUser.setName("updatedName");
        transactionManager.doInTransaction(session -> {
            userTemplate.update(session, updatedUser);
            return null;
        });

        //then
        var loadedUser = transactionManager.doInReadOnlyTransaction(session ->
                userTemplate.findById(session, updatedUser.getId())
        );
        assertThat(loadedUser).isPresent().get().usingRecursiveComparison().isEqualTo(updatedUser);

        //when
        var userList = transactionManager.doInReadOnlyTransaction(session ->
                userTemplate.findAll(session)
        );

        //then
        assertThat(userList.size()).isEqualTo(1);
        assertThat(userList.get(0)).usingRecursiveComparison().isEqualTo(updatedUser);


        //when
        userList = transactionManager.doInReadOnlyTransaction(session ->
                userTemplate.findByEntityField(session, "name", "updatedName")
        );

        //then
        assertThat(userList.size()).isEqualTo(1);
        assertThat(userList.get(0)).usingRecursiveComparison().isEqualTo(updatedUser);
    }
}
