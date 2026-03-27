//ClientController.java
package com.ayur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<Client> getAll(@RequestParam(required = false) String search) {
        List<Client> all = clientRepository.findAll();
        if (search != null && !search.isEmpty()) {
            String query = search.toLowerCase();
            return all.stream()
                .filter(c -> "PATIENT".equals(c.getRole())) // Only patients in search
                .filter(c -> c.getName().toLowerCase().contains(query) || 
                             (c.getMedicineBranch() != null && c.getMedicineBranch().toLowerCase().contains(query)))
                .collect(Collectors.toList());
        }
        return all; 
    }

    @PostMapping
    public Client save(@RequestBody Client client) {
        return clientRepository.save(client);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable Long id, @RequestBody Client details) {
        Client client = clientRepository.findById(id).orElseThrow();
        client.setName(details.getName());
        client.setMedicineBranch(details.getMedicineBranch());
        client.setPrakriti(details.getPrakriti());
        client.setAgniStrength(details.getAgniStrength());
        client.setProgressNotes(details.getProgressNotes());
        client.setCurrentRoom(details.getCurrentRoom());
        return clientRepository.save(client);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientRepository.deleteById(id);
    }
}