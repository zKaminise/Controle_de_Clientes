package com.example.PsicoManagerProject.Service;

import com.example.PsicoManagerProject.Entitys.Client;
import com.example.PsicoManagerProject.Exceptions.ClientNotFoundException;
import com.example.PsicoManagerProject.Repositorys.ClientRepository;
import com.example.PsicoManagerProject.Repositorys.FinanceiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private FinanceiroRepository financeiroRepository;

    @Transactional
    public Client novoCliente (Client client) {
        if (clientRepository.existsByCpf(client.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        return clientRepository.save(client);
    }

    public Optional<Client> buscarClientePorCpf(String cpf) {
        return clientRepository.findByCpf(cpf);
    }

    @Transactional
    public void deleteClientByCpf(String cpf) throws ClientNotFoundException {
        Client client = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado com o CPF fornecido: " + cpf));

        financeiroRepository.deleteByClientId(client.getId());

        clientRepository.delete(client);
    }



}
