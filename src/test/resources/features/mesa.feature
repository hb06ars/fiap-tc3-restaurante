# language: pt
# encoding: UTF-8

Funcionalidade: API - Mesa

  Cenário: Atualizar Mesa
    Dado submeter um novo Restaurante
    Dado que uma Mesa já exista no sistema
    Quando requisitar a alteração da Mesa
    Então a Mesa é atualizada com sucesso

  Cenário: Buscar Mesas por restaurante
    Dado que uma Mesa já exista
    Quando requisitar a busca da Mesa
    Então a Mesa é exibida com sucesso

  Cenário: Buscar Mesas disponíveis por Restaurante
    Dado que uma Mesa já exista para o restaurante
    Quando requisitar a busca da Mesa pelo Id do Restaurante
    Então as Mesas são exibidas com sucesso




