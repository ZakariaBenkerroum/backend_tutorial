package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Client", description = "API of Client")
@RequestMapping(path = "/client")
@RestController
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    ModelMapper mapper;

    private Map<Long, ClientDto> clients = new HashMap<Long, ClientDto>();

    @Operation(summary = "Find ", description = "Method that returns a list of clients")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ClientDto> findAll() {
        // We just connect the controller with the Service that implements the logic.
        List<Client> clients = this.clientService.findAll();
        // Conversion from Client to ClientDto
        return clients.stream().map(e -> mapper.map(e, ClientDto.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Save or Update", description = "Method that saves or updates a client")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody ClientDto dto) throws Exception {

        this.clientService.save(id, dto);
    }

    @Operation(summary = "Delete", description = "Method that delete a client")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(name = "id") Long id) throws Exception {
        this.clientService.delete(id);
    }

}
