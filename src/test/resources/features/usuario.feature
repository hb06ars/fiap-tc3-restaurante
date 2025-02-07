# language: pt
# encoding: UTF-8

Funcionalidade: API - Usuário

  Cenário: Cadastrar um Usuário
    Quando submeter um novo Usuário
    Então o Usuário é salvo com sucesso

  Cenário: Atualizar Usuário
    Dado que um Usuário já exista no sistema
    Quando requisitar a alteração do Usuário
    Então o Usuário é atualizado com sucesso

  Cenário: Buscar Usuário
    Dado que um Usuário já exista
    Quando requisitar a busca do Usuário
    Então o Usuário é exibido com sucesso




