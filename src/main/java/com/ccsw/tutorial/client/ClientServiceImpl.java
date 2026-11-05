package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Client get(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Client> findAll() {
        return (List<Client>) this.clientRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, ClientDto dto) throws Exception {

        Client client;

        if (id == null) {
            client = new Client();

        } else {
            client = this.get(id);
        }

        client.setName(dto.getName());
        Client clientExistente = this.clientRepository.findByName(dto.getName());

        if (clientExistente != null && !clientExistente.getId().equals(id)) {
            throw new Exception("El cliente ya existe!");
        }

        this.clientRepository.save(client);
    }

    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("The client you are trying to delete doesn't exists");
        }

        this.clientRepository.deleteById(id);
    }

}
