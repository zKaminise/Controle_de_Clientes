package com.example.PsicoManagerProject.Controller;

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
    @Operation(summary = "Listar todos os clientes retornado um informação basica, será usado na tela home do aplicativo", description = "Essa função é responsável por listar todos clientes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Client.class))
            })
    })
    public List<Object[]> getClientsBasicInfo() {
        return clientRepository.findAll()
                .stream()
                .map(client -> new Object[]{
                        client.getNome(),
                        client.getCpf(),
                        client.getEmail(),
                        client.getDataNascimento(),
                        client.getRecebeuAltaEnum()
                }).toList();
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
    @Operation(summary = "Alterar dados do Cliente", description = "Essa função é responsável por as informações do cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Client.class))
            }),
            @ApiResponse(responseCode = "400", description = "Não foi encontrado cliente com esse CPF")
    })
    public String updateClient(@PathVariable String cpf, @RequestBody Client updatedClient) {
        var existingClient = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClientNotFoundException("Cliente com CPF: " + cpf + " não foi encontrado!"));

        updatedClient.setId(existingClient.getId());
        clientRepository.save(updatedClient);

        return "Cliente atualizado com sucesso!";
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Clientes filtrados por CPF", description = "Essa função é responsável por excluir com base no CPF")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Client.class))
            }),
            @ApiResponse(responseCode = "400", description = "Não foi encontrado cliente com esse CPF")
    })
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado com sucessoo!");
        } catch (ClientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado!");
        }
    }
}
