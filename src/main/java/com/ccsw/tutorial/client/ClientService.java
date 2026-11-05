package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientService {

    Client get(Long id);

    List<Client> findAll();

    void save(Long id, ClientDto dto) throws Exception;

    void delete(Long id) throws Exception;
}
