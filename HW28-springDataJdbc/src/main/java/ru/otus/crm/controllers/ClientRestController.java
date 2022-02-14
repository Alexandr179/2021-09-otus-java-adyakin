package ru.otus.crm.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.crm.model.Client;
import ru.otus.crm.services.ClientService;

@RestController
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }


//    @GetMapping("/api/client/{id}")
//    public Optional<Client> getClientById(@PathVariable(name = "id") long id) {
//        return clientService.getClient(id);
//    }

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody Client client) {
        return clientService.saveClient(client);
    }
}
