package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    Map<Long, Client> cache = new WeakHashMap<>();

//    private final HwCache<Long, Client> cache = new HwCacheImpl<>();

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client client = cache.get(id);
        if (client == null) {
            Optional<Client> optClient = transactionManager.doInReadOnlyTransaction(session -> {
                var clientOptional = clientDataTemplate.findById(session, id);
                log.info("optClient: {}", clientOptional);
                return clientOptional;
            });
            optClient.ifPresent(existsClient -> cache.put(id, existsClient));
            return optClient;
        }
        log.warn("CacheClient: {}", client);
        return Optional.of(client);
    }


    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
