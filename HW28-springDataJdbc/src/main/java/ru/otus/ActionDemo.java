package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.crm.model.Client;
import ru.otus.crm.services.ClientService;

@Component("actionDemo")
public class ActionDemo {
    private static final Logger log = LoggerFactory.getLogger(ActionDemo.class);

    private final ClientService clientService;

    public ActionDemo(ClientService clientService) {
        this.clientService = clientService;
    }

    void action() {
        var firstClient = clientService.saveClient(new Client("FirstClient", "First", "pass"));
        var secondClient = clientService.saveClient(new Client("SecondClient", "Second", "pass"));
        var therdClient = clientService.saveClient(new Client("TherdClient", "Therd", "pass"));
    }
}
