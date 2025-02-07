# language: pt
Funcionalidade: API - Reserva

  Cenário: Cadastrar uma Reserva
    Quando submeter uma nova Reserva
    Então a Reserva é salva com sucesso

  Cenário: Atualizar Reserva
    Dado que uma Reserva já exista no sistema
    Quando requisitar a alteração da Reserva
    Então a Reserva é atualizado com sucesso

  Cenário: Buscar Reserva
    Dado que um Reserva já exista
    Quando requisitar a busca da Reserva
    Então a Reserva é exibida com sucesso




