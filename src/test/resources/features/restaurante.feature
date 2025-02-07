# language: pt
Funcionalidade: API - Restaurante

  Cenário: Cadastrar um Restaurante
    Quando submeter um novo Restaurante
    Então o Restaurante é salvo com sucesso

  Cenário: Atualizar Restaurante
    Dado que um Restaurante já exista no sistema
    Quando requisitar a alteração do Restaurante
    Então o Restaurante é atualizado com sucesso

  Cenário: Deletar Restaurante
    Dado que um Restaurante já foi salvo
    Quando requisitar a exclusão do Restaurante
    Então o Restaurante é removido com sucesso

  Cenário: Buscar Restaurante
    Dado que um Restaurante já exista
    Quando requisitar a busca do Restaurante
    Então o Restaurante é exibido com sucesso




