
# Projeto de Testes Automatizados - Gerenciamento de Semáforos

Este repositório contém o código-fonte dos testes automatizados para a API de gerenciamento de semáforos. A aplicação utiliza uma API baseada em Spring Boot e testes BDD escritos com Cucumber e RestAssured, com verificação de contrato JSON.

## Índice

1. [Pré-requisitos](#pré-requisitos)
2. [Configuração do Ambiente](#configuração-do-ambiente)
3. [Execução dos Testes](#execução-dos-testes)
4. [Estrutura do Projeto](#estrutura-do-projeto)
5. [Detalhes dos Testes](#detalhes-dos-testes)

---

### Pré-requisitos

- **Docker**: Certifique-se de que o Docker está instalado para rodar o container da API.
- **Java 17**: A aplicação e os testes utilizam o Java 17.
- **Maven**: Gerenciador de dependências para compilar e executar os testes.

### Configuração do Ambiente

1. **Baixar a imagem Docker da API**: Execute o comando abaixo para rodar a API localmente em um container Docker:
   ```bash
   docker run -p 8080:8080 rm93287/traffic_management:10f4fd8845cc47d5f936702dce5a7e6fae1fe012
   ```

2. **Configurar o projeto de testes**:
    - Clone este repositório.
    - Garanta que as dependências estão instaladas executando:
      ```bash
      mvn clean install
      ```

### Execução dos Testes

Para executar os testes automatizados, utilize o seguinte comando:

```bash
mvn clean test
```

### Estrutura do Projeto

- **src/test/resources/features**: Arquivos `.feature` descrevendo os cenários de teste em Gherkin.
    - `GerenciamentoSemaforos.feature`: Testa o cadastro e validação de campos obrigatórios para criação de semáforos.
    - `DelecaoSemaforos.feature`: Testa a deleção de um semáforo recém-criado.
- **src/test/java/steps**: Arquivo `CadastroSemaforosSteps.java` contendo os passos dos testes BDD.
- **src/test/java/services**: Arquivo `CadastroSemaforosService.java` com a lógica de requisições HTTP e validação de contrato JSON.
- **src/test/resources/schemas**: Arquivos JSON com o contrato de resposta esperado para os testes.
- **src/test/java/models**: Modelos de dados `SemaforoModel` e `StatusErrorMessageModel`, usados para representar dados nos testes.

### Detalhes dos Testes

1. **Gerenciamento de Semáforos**:
    - **Cenário**: Cadastro bem-sucedido de semáforo.
        - **Descrição**: Autentica o usuário, envia dados válidos de um semáforo e verifica o status de criação (`201`), além da conformidade da resposta com o contrato JSON.
    - **Cenário**: Erro ao tentar cadastrar um semáforo sem informar um campo obrigatório.
        - **Descrição**: Tenta criar um semáforo sem o campo `status` e valida o retorno de erro (`400`) e a mensagem de erro correspondente.

2. **Deleção de Semáforo**:
    - **Contexto**: Cadastro bem-sucedido de semáforo.
        - **Descrição**: Autentica o usuário, cria um semáforo e armazena seu `ID`.
    - **Cenário**: Deleção de um semáforo com sucesso.
        - **Descrição**: Recupera o `ID` do semáforo criado no contexto e envia uma requisição `DELETE` para remover o semáforo, validando o status de resposta (`204`).

--- 
