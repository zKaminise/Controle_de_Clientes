# Sistema de Gestão de Clientes

## Descrição

Este projeto é um Sistema de Gestão de Clientes, desenvolvido com o objetivo de permitir cadastro de clientes, atualização e exclusão, permite também usuario ter um segurança no Login e registro de usuarios através do Spring Security e também temos a parte do Financeiro
onde o usuario pode Cadastrar um pagamento de cada cliente, sendo filtrado por CPF, emitir relatórios e recibos de pagamento e baixar o arquivo PDF.

## Oque falta ser feito:

- Front End para a aplicação
- Realizar Deploy para Produção


## Tecnologias Usadas:

- **Java**: Versão 17
- **Spring Boot**
- **Spring Security**: Para autenticação e autorização
- **Spring Data JPA**: Para interação com o Banco de Dados
- **Jasper Reports**: Gerar os arquivos PDF
- **Docker**: Para Conteinerização do projeto
- **Swagger**: Para documentação da API
- **SonarQube** e **Jakoku**: Para defiinir métricas de qualidade, como quantidade de bugs, códigos duplicados, e verificar a segurança das dependencias. E uso do Jakoku para verificar cobertura de testes, medir eficiencias dos testes e definir um minimo de cobertura esperado.
- **Actuator**: Para disponibilizar as rotas e assim visializar as métricas e informações da aplicação
- **Grafana** e **Prometheus**: Para monitoramento dos serviços, usando o Prometheus para atuar junto ao BackEnd gerando as métricas das aplicações e o Grafana para gerar os gráficos com as informações em tempo real

## Imagens

Grafana e Prometheus(DataSource)

![GrafanaPsicoProject](https://github.com/user-attachments/assets/69bf7b00-6c41-4147-8d8b-28fdf5a13150)



##

SonarQube e Jakoku

![SonarQubePsicoProject](https://github.com/user-attachments/assets/a4412505-8619-46b4-b14f-c3a505b1e330)


## Pré-requisitos

 - Java 17
 - Maven
 - IDE de sua escolha (IntelliJ, Eclipse, etc.)
 - Banco de Dados PostgreSQL
 - Docker instalado na sua máquina

## Instalação

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/zKaminise/Controle-de-Cliente-Psicologia-.git
   cd Controle-de-Cliente-Psicologia-
   ```

2. **Configuração e execução com Docker:**

   - Com o Docker e Docker Compose instalados, execute o ambiente completo (banco de dados e aplicação) com o comando:

     ```bash
     docker-compose up -d
     ```
   - **Estou finalizando toda configuração de Docker para funcionamento 100%**

3. **Acesso a aplicação**

   - A aplicação estará disponivel em:
     ```
     http://localhost:8080
     ```
   - O Grafana estará disponivel em:
     ```
     http://localhost:3000
     ```
   - O SonarQube estará disponivel em:
    - Rodar o seguinte comando no terminal para rodar o SonarQube
     ```
     docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:9.9.0-community
     ```
  
   - Necessário rodar o seguinte comando no terminal do Projeto:
     ```
     mvn clean verify sonar:sonar
     ```
    - E então acessar o link abaixo: (Login padrão é usuario e senha "admin")
       ```
       http://localhost:9000
       ```

### Endpoints Principais

- **Financeiro**
    - `POST /financeiro`: Cadastrar pagamento do Cliente
    - `GET /financeiro/report`: Relatório dos pagamentos cadastrados
    - `GET /financeiro/receipt{id}`: Recibo de pagamento do Cliente
      
- **Clientes**
    - `GET /clients`: Listar todos clientes
    - `GET /clients/{cpf}`: Listar Cliente por CPF
    - `GET /clients/home-info`: Lista todos clientes, porém retornando somente informações basicas
    - `POST /clients`: Cadastrar Novo Cliente
    - `PUT /clients/{cpf}`: Alterar informações do Cliente filtrado por CPF
    - `DELETE /clients/{id}`: Excluir cliente da base de dados

 - **Login**
   - `POST /auth/login`: Realizar Login no Sistema
   - `POST /auth/register`: Registrar um novo usuario e senha
   - `POST /auth/reset-password`: Alterar senha
   - `POST /auth/request-reset-password`: Solicitar um reset de senha

## Documentação

A documentação da API está disponivel através do Swagger no URL:

```
http://localhost:8080/swagger-ui/index.html
```

## Autor

**Gabriel Misao Pinheiro Kaminise**

https://linkedin.com/in/gabrielkaminise

## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).
