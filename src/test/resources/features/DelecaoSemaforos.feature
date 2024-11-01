#language: pt

@regressivo
Funcionalidade: Deletar um semáforo
  Como usuário da API
  Quero deletar um semáforo
  Para que o registro seja deletado do sistema
  Contexto: Cadastro bem-sucedido de semáforo
    Dado que eu estou autenticado com email "rm93287@fiap.com.br" e senha "123456@"
    E que eu tenha os seguintes dados do semáforo:
      | campo  | valor                |
      | lat    | -18.739267137971055  |
      | lng    | -39.74751897551345   |
      | status | red                  |
    Quando eu enviar a requisição para o endpoint "/api/traffic-light" de cadastro de semáforos
    Então o status code da resposta de criacao deve ser 201

  Cenário: Deleção de um semáforo com sucesso
    Dado que eu estou autenticado com email "rm93287@fiap.com.br" e senha "123456@"
    E que eu recupere o ID do semáforo criado no contexto
    Quando eu enviar a requisição DELETE para o endpoint "/api/traffic-light" com o ID do semáforo
    Então o status code da resposta de delecao deve ser 204

