# language: pt
Funcionalidade: API - Avaliação

  Cenário: Cadastrar uma avaliacao
    Quando submeter uma nova mensagem
    Então a mensagem é registrada com sucesso

  Cenário: Listar Avaliações pelo Restaurante
    Quando requisitar a lista da mensagem
    Então as avaliações sao exibidas com sucesso

