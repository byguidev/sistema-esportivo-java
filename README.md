# Sistema Esportivo

Sistema desktop em Java 17 com JavaFX e arquitetura em três camadas para cadastro de competições, inscrição de atletas e publicação de resultados/classificação.

## Requisitos atendidos

- Tela JavaFX para cadastro de competições, inscrição de atletas e visualização de resultados/classificação.
- Validação de nome da competição, data e categoria do atleta.
- Regras de negócio para limite de participantes e inscrição única por atleta.
- Armazenamento em memória com `ArrayList`.
- Operações CRUD no repositório para competição, atleta, inscrição e resultado.

## Como executar

1. Compilar e executar os testes:

```bash
mvn test
```

2. Executar a aplicação desktop:

```bash
mvn javafx:run
```

## Estrutura das camadas

- `presentation`: camada de apresentação JavaFX.
- `business`: regras de negócio e validações.
- `data`: repositório em memória.
- `model`: entidades do domínio.

## Diagrama de classes

> As quatro entidades formam um ciclo (Atleta → Inscrição → Competição → Resultado → Atleta); por isso as classes abaixo são declaradas nessa mesma ordem, o que evita cruzamento de linhas no layout automático do Mermaid.

```mermaid
classDiagram
    direction LR

    class Atleta {
        <<entity>>
        -long id
        -String nome
        -String categoria
    }

    class Inscricao {
        <<entity>>
        -long id
        -Atleta atleta
        -Competicao competicao
    }

    class Competicao {
        <<entity>>
        -long id
        -String nome
        -LocalDate data
        -int limiteParticipantes
    }

    class Resultado {
        <<entity>>
        -long id
        -Competicao competicao
        -Atleta primeiroLugar
        -Atleta segundoLugar
        -Atleta terceiroLugar
    }

    class ServicoAtividadesEsportivas {
        <<business>>
        +criarCompeticao(Competicao) void
        +criarAtleta(Atleta) void
        +criarInscricao(Inscricao) void
        +criarResultado(Resultado) void
        +validarLimiteParticipantes(Competicao) boolean
        +validarInscricaoUnica(Competicao, Atleta) boolean
    }

    class RepositorioAtividades {
        <<data>>
        +salvarCompeticao(Competicao) void
        +salvarAtleta(Atleta) void
        +salvarInscricao(Inscricao) void
        +salvarResultado(Resultado) void
    }

    Atleta "1" -- "0..*" Inscricao : inscrições
    Inscricao "0..*" -- "1" Competicao : competição
    Competicao "1" -- "0..*" Resultado : resultados
    Resultado "0..*" -- "1" Atleta : pódio
    ServicoAtividadesEsportivas ..> RepositorioAtividades : usa
```

## Diagrama de sequência: cadastrar inscrição

```mermaid
sequenceDiagram
    actor Usuario
    participant Tela as Tela JavaFX
    participant Servico as ServicoAtividadesEsportivas
    participant Repo as RepositorioAtividades

    Usuario->>Tela: Cadastrar competição/inscrição
    Tela->>Servico: processar(...)
    Servico->>Servico: validarLimiteParticipantes()
    Servico->>Servico: validarInscricaoUnica()

    alt Regras atendidas
        Servico->>Repo: salvarInscricao()
        Repo-->>Servico: sucesso
        Servico-->>Tela: sucesso
        Tela-->>Usuario: Mensagem de sucesso
    else Regra violada
        Servico-->>Tela: mensagem de erro
        Tela-->>Usuario: Exibir alerta
    end
```

## Verificação sugerida

- Abrir a aplicação e cadastrar uma competição com limite de participantes.
- Cadastrar atletas e realizar inscrições.
- Tentar duplicar inscrição do mesmo atleta na mesma competição.
- Completar o limite da competição e tentar uma nova inscrição.
- Abrir a tela principal e conferir os resultados publicados.