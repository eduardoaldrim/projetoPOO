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
  entidades/           classes de domínio (pacote entidades)
    Monitoravel.java   interface de cálculo de calorias
    Exercicio.java     classe abstrata base dos exercícios
  aplicacao/           ponto de entrada e menus (pacote aplicacao)
    Programa.java      (a fazer)
```

## Progresso

- [x] `Monitoravel`
- [x] `Exercicio`
- [ ] `ExercicioDeForca`, `ExercicioCardio`, `ExercicioFuncional`
- [ ] `Treino`, `Aluno`, `RegistroDeTreino`
- [ ] `RepositorioDeDados<T>`, `RepositorioEmLista<T>`
- [ ] Serviços (`ServicoDeAlunos`, `ServicoDeExercicios`, `ServicoDeTreinos`, `ServicoDeRegistros`)
- [ ] `Programa.java` com menus e 7 objetos iniciais de cada classe

## Como compilar e executar

Requer JDK 17+.

```bash
javac -d out src/entidades/*.java src/aplicacao/*.java
java -cp out aplicacao.Programa
```

## Entrega

24/11/2026 — classes copiadas para um TXT com o nome completo dos integrantes no topo,
postado no Google Classroom.
