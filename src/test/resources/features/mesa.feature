# language: pt
Funcionalidade: API - Mesa

  Cenário: Cadastrar um Mesa
    Quando submeter uma nova Mesa
    Então ao Mesa é salva com sucesso

  Cenário: Atualizar Mesa
    Dado que um Mesa já exista no sistema
    Quando requisitar a alteração da Mesa
    Então a Mesa é atualizada com sucesso

  Cenário: Buscar Mesa
    Dado que um Mesa já exista
    Quando requisitar a busca da Mesa
    Então a Mesa é exibida com sucesso

  Cenário: Buscar Mesas por Restaurante
    Dado que um Mesa já exista para o restaurante
    Quando requisitar a busca da Mesa pelo Id do Restaurante
    Então as Mesas são exibidas com sucesso




