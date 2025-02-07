# language: pt
# encoding: UTF-8

Funcionalidade: API - Avaliação

  Cenário: Cadastrar uma avaliacao
    Dado submeter um novo Usuário
    Dado submeter um novo Restaurante
    Quando submeter uma nova mensagem
    Então a mensagem é registrada com sucesso

  Cenário: Listar Avaliações pelo Restaurante
    Dado que uma avaliacao ja exista
    Quando requisitar a lista da mensagem
    Então as avaliações sao exibidas com sucesso

