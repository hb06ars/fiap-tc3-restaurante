# language: pt
# encoding: UTF-8

Funcionalidade: API - Funcionamento

  Cenário: Cadastrar um Horário de Funcionamento
    Quando submeter um novo Horário de Funcionamento
    Então o Horário de Funcionamento é salvo com sucesso

  Cenário: Atualizar Horário de Funcionamento
    Dado que um Horário de Funcionamento já exista no sistema
    Quando requisitar a alteração do Horário de Funcionamento
    Então o Horário de Funcionamento é atualizado com sucesso

  Cenário: Deletar Horário de Funcionamento
    Dado que um Horário de Funcionamento já foi salvo
    Quando requisitar a exclusão do Horário de Funcionamento
    Então o Horário de Funcionamento é removido com sucesso

  Cenário: Buscar Horários de Funcionamento do restaurante
    Dado que um Horário de Funcionamento já exista
    Quando requisitar a busca do Horário de Funcionamento
    Então o Horário de Funcionamento é exibido com sucesso




