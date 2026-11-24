# Gerenciador de Treinos de Academia

Projeto da disciplina de Programação Orientada a Objetos (Prof. Artur Henrique Kronbauer).

Sistema em Java, com interface textual, para cadastrar alunos, montar treinos com exercícios
e registrar os treinos realizados. A especificação completa está em
`Descrição Projeto - T5 - Mat.pdf` (requisitos) e `Gerenciador_de_Treinos_Academia.pdf` (proposta).

## Conceitos de POO cobertos

| Conceito | Onde |
|---|---|
| Classe abstrata | `Exercicio` |
| Interface | `Monitoravel` (`estimarCaloriasGastas()`), `RepositorioDeDados<T>` |
| Herança | `ExercicioDeForca`, `ExercicioCardio`, `ExercicioFuncional` estendem `Exercicio` |
| Polimorfismo | cada exercício calcula calorias do seu jeito; `Treino` soma sem saber o tipo |
| Composição | `Treino` contém lista de `Exercicio`; `Aluno` contém lista de `Treino` |
| IoC / DI | serviços recebem `RepositorioDeDados<T>` pelo construtor; `Programa` monta tudo |

## Estrutura

```
src/
  modelo/              classes de domínio
    Monitoravel.java   interface de cálculo de calorias
    Exercicio.java     classe abstrata base dos exercícios
    ExercicioDeForca.java, ExercicioCardio.java, ExercicioFuncional.java
    Treino.java, Aluno.java, RegistroDeTreino.java
    enumeradas/        Objetivo, Intensidade
  repositorio/         RepositorioDeDados<T> (interface) e RepositorioEmLista<T> (ArrayList)
  servico/             ServicoDeAlunos, ServicoDeExercicios, ServicoDeTreinos, ServicoDeRegistros
  aplicacao/
    Programa.java      main, menus, dados iniciais (7 de cada classe)
```

## Progresso

- [x] `Monitoravel`, `Exercicio` e as 3 subclasses
- [x] `Treino`, `Aluno`, `RegistroDeTreino`
- [x] `RepositorioDeDados<T>`, `RepositorioEmLista<T>`
- [x] Serviços (`ServicoDeAlunos`, `ServicoDeExercicios`, `ServicoDeTreinos`, `ServicoDeRegistros`)
- [x] `Programa.java` com menus e 7 objetos iniciais de cada classe
- [x] Diferenciais: alerta de inatividade e validação de entrada

## Como compilar e executar

Requer JDK 17+.

```bash
javac -encoding UTF-8 -d out src/modelo/*.java src/modelo/enumeradas/*.java src/repositorio/*.java src/servico/*.java src/aplicacao/*.java
java -cp out aplicacao.Programa
```

## Entrega

24/11/2026 — classes copiadas para um TXT com o nome completo dos integrantes no topo,
postado no Google Classroom.
