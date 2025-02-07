# language: pt
# encoding: UTF-8

Funcionalidade: API - Reserva

  Cenário: Cadastrar uma Reserva
    Dado submeter um novo Usuário
    Dado submeter um novo Restaurante
    Quando submeter uma nova Reserva
    Então a Reserva é salva com sucesso

  Cenário: Atualizar Reserva
    Dado que uma Reserva já exista no sistema
    Quando requisitar a alteração da Reserva
    Então a Reserva é atualizada com sucesso

  Cenário: Buscar Reserva
    Dado que um Reserva já exista
    Quando requisitar a busca da Reserva
    Então a Reserva é exibida com sucesso




