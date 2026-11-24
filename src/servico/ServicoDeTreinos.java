package servico;

import modelo.Exercicio;
import modelo.Treino;
import modelo.enumeradas.Objetivo;
import repositorio.RepositorioDeDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Regras de negocio dos treinos.
// Recebe dois repositorios: o dos treinos e o dos exercicios. O de exercicios é pra conferir
// se o exercicio que a pessoa quer colocar no treino existe mesmo.
public class ServicoDeTreinos {
    private RepositorioDeDados<Treino> repositorioTreinos;
    private RepositorioDeDados<Exercicio> repositorioExercicios;

    public ServicoDeTreinos(RepositorioDeDados<Treino> repositorioTreinos, RepositorioDeDados<Exercicio> repositorioExercicios) {
        this.repositorioTreinos = repositorioTreinos;
        this.repositorioExercicios = repositorioExercicios;
    }

    public Treino cadastrar(Treino treino) {
        treino.setId(gerarProximoId());
        repositorioTreinos.inserir(treino);
        return treino;
    }

    // coloca o exercicio no treino, mas só se ele estiver cadastrado. Devolve false se não achou
    public boolean adicionarExercicio(Treino treino, int idExercicio) {
        Exercicio exercicio = repositorioExercicios.buscarPorId(idExercicio);
        if (treino == null || exercicio == null) {
            return false;
        }
        treino.getExercicios().add(exercicio);
        return true;
    }

    // quando um exercicio é excluido, tira ele de todos os treinos que usavam
    public void removerExercicioDosTreinos(Exercicio exercicio) {
        for (Treino treino : repositorioTreinos.listarTodos()) {
            treino.getExercicios().remove(exercicio);
        }
    }

    public boolean alterar(Treino treinoAtualizado) {
        return repositorioTreinos.atualizar(treinoAtualizado.getId(), treinoAtualizado);
    }

    public boolean remover(int id) {
        return repositorioTreinos.remover(id);
    }

    public Treino buscarPorId(int id) {
        return repositorioTreinos.buscarPorId(id);
    }

    public List<Treino> listarTodos() {
        return repositorioTreinos.listarTodos();
    }

    // busca por atributo: todos os treinos com aquele objetivo
    public List<Treino> buscarPorObjetivo(Objetivo objetivo) {
        List<Treino> resultado = new ArrayList<>();
        for (Treino treino : repositorioTreinos.listarTodos()) {
            if (treino.getObjetivo() == objetivo) {
                resultado.add(treino);
            }
        }
        return resultado;
    }

    public List<Treino> listarOrdenadoPorNome() {
        List<Treino> lista = repositorioTreinos.listarTodos();
        lista.sort(Comparator.comparing(Treino::getNome));
        return lista;
    }

    // ordena na ordem que os objetivos aparecem no enum (hipertrofia, emagrecimento, condicionamento)
    public List<Treino> listarOrdenadoPorObjetivo() {
        List<Treino> lista = repositorioTreinos.listarTodos();
        lista.sort(Comparator.comparing(Treino::getObjetivo));
        return lista;
    }

    private int gerarProximoId() {
        int maiorId = 0;
        for (Treino treino : repositorioTreinos.listarTodos()) {
            if (treino.getId() > maiorId) {
                maiorId = treino.getId();
            }
        }
        return maiorId + 1;
    }
}
