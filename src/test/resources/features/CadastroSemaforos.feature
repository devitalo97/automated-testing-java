#language: pt

@regressivo
Funcionalidade: Gerenciamento de Semáforos
  Como usuário da API
  Quero gerenciar os semáforos
  Para que eles sejam registrados e atualizados corretamente no sistema
  Cenário: Cadastro bem-sucedido de semáforo
    Dado que eu estou autenticado com email "rm93287@fiap.com.br" e senha "123456@"
    E que eu tenha os seguintes dados do semáforo:
      | campo  | valor                |
      | lat    | -18.739267137971055  |
      | lng    | -39.74751897551345   |
      | status | red                  |
    Quando eu enviar a requisição para o endpoint "/api/traffic-light" de cadastro de semáforos
    Então o status code da resposta de criacao deve ser 201
    E que o arquivo de contrato esperado é o "Cadastro bem-sucedido de semaforo"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado

  Cenário: Erro ao tentar cadastrar um semáforo sem informar um campo obrigatório
    Dado que eu estou autenticado com email "rm93287@fiap.com.br" e senha "123456@"
    E que eu tenha os seguintes dados do semáforo:
      | campo | valor                |
      | lat   | -18.739267137971055  |
      | lng   | -39.74751897551345   |
    Quando eu enviar a requisição para o endpoint "/api/traffic-light" de cadastro de semáforos
    Então o status code da resposta de criacao deve ser 400
    E a mensagem de erro deve informar "Status field is required"

