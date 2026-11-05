package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {

    // Adding this function to check if the name of the new client equals one of the client's name that exists in the data base
    Client findByName(String name);
}
