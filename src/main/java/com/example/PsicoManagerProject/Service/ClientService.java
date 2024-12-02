package com.example.PsicoManagerProject.Service;

import com.example.PsicoManagerProject.Entitys.Client;
import com.example.PsicoManagerProject.Exceptions.ClientNotFoundException;
import com.example.PsicoManagerProject.Repositorys.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client novoCliente (Client client) {
        if (clientRepository.existsByCpf(client.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        return clientRepository.save(client);
    }

    public Optional<Client> buscarClientePorCpf(String cpf) {
        return clientRepository.findByCpf(cpf);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não foi encontrado"));
        clientRepository.delete(client);
    }
}
