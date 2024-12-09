package com.example.PsicoManagerProject.Controller;

import com.example.PsicoManagerProject.Dtos.ClientDto;
import com.example.PsicoManagerProject.Entitys.Client;
import com.example.PsicoManagerProject.Exceptions.ClientNotFoundException;
import com.example.PsicoManagerProject.Exceptions.InvalidCpfException;
import com.example.PsicoManagerProject.Repositorys.ClientRepository;
import com.example.PsicoManagerProject.Service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clientes", description = "Informações dos Clientes")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @PostMapping
    @Operation(summary = "Cadastrar novo Cliente", description = "Essa função é responsável por cadastrar novos clientes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Client.class))
            }),
            @ApiResponse(responseCode = "400", description = "Cliente já existe")
    })
    public ResponseEntity<String> addClient(@RequestBody Client client) {
        try {
            clientService.novoCliente(client);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Cliente Cadastrado com Sucesso!");
        } catch (InvalidCpfException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar Todos Clientes", description = "Essa função é responsável por listar todos clientes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Client.class))
            })
    })
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/home-info")
    @Operation(summary = "Listar todos os clientes retornando informações básicas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ClientDto.class))
            })
    })
    public List<ClientDto> getClientsBasicInfo() {
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDto(
                        client.getNome(),
                        client.getCpf(),
                        client.getEmail(),
                        client.getDataNascimento().toString(),
                        client.getDataInicioTratamento().toString(),
                        client.getDataFimTratamento() != null ? client.getDataFimTratamento().toString() : null
                ))
                .toList();
    }

    @GetMapping("/{cpf}")
    @Operation(summary = "Listar Clientes filtrados por CPF", description = "Essa função é responsável por listar clientes com base no CPF pesquisado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Client.class))
            }),
            @ApiResponse(responseCode = "400", description = "Não foi encontrado cliente com esse CPF")
    })
    public ResponseEntity<Client> getClient(@PathVariable String cpf) {
        Optional<Client> client = clientService.buscarClientePorCpf(cpf);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }


    @PutMapping("/{cpf}")
    public ResponseEntity<?> updateClient(@PathVariable String cpf, @RequestBody Client updatedClient) {
        try {
            Client existingClient = clientRepository.findByCpf(cpf)
                    .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado"));

            updatedClient.setId(existingClient.getId());
            clientRepository.save(updatedClient);

            return ResponseEntity.ok("Cliente atualizado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar cliente: " + e.getMessage());
        }
    }



    @DeleteMapping("/{cpf}")
    @Operation(summary = "Deletar Clientes filtrados por CPF", description = "Essa função é responsável por excluir clientes com base no CPF")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente deletado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado com o CPF fornecido!")
    })
    public ResponseEntity<String> deleteClient(@PathVariable String cpf) {
        try {
            clientService.deleteClientByCpf(cpf);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado com sucesso!");
        } catch (ClientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao tentar deletar o cliente: " + e.getMessage());
        }
    }


}
