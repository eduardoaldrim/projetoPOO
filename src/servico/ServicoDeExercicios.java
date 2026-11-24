package servico;

import modelo.Exercicio;
import repositorio.RepositorioDeDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Regras de negocio dos exercicios. Trabalha com o tipo Exercicio (a classe mae),
// entao serve pros 3 tipos sem precisar de um serviço pra cada.
public class ServicoDeExercicios {
    private RepositorioDeDados<Exercicio> repositorio;

    // recebe o repositorio pronto (injeção de dependencia)
    public ServicoDeExercicios(RepositorioDeDados<Exercicio> repositorio) {
        this.repositorio = repositorio;
    }

    public Exercicio cadastrar(Exercicio exercicio) {
        exercicio.setId(gerarProximoId());
        repositorio.inserir(exercicio);
        return exercicio;
    }

    public boolean alterar(Exercicio exercicioAtualizado) {
        return repositorio.atualizar(exercicioAtualizado.getId(), exercicioAtualizado);
    }

    public boolean remover(int id) {
        return repositorio.remover(id);
    }

    public Exercicio buscarPorId(int id) {
        return repositorio.buscarPorId(id);
    }

    public List<Exercicio> listarTodos() {
        return repositorio.listarTodos();
    }

    // busca por atributo: grupo muscular (ex: digitar "perna" acha "Pernas")
    public List<Exercicio> buscarPorGrupoMuscular(String grupoMuscular) {
        List<Exercicio> resultado = new ArrayList<>();
        for (Exercicio exercicio : repositorio.listarTodos()) {
            if (exercicio.getGrupoMuscular().toLowerCase().contains(grupoMuscular.toLowerCase())) {
                resultado.add(exercicio);
            }
        }
        return resultado;
    }

    public List<Exercicio> listarOrdenadoPorNome() {
        List<Exercicio> lista = repositorio.listarTodos();
        lista.sort(Comparator.comparing(Exercicio::getNome));
        return lista;
    }

    public List<Exercicio> listarOrdenadoPorGrupoMuscular() {
        List<Exercicio> lista = repositorio.listarTodos();
        lista.sort(Comparator.comparing(Exercicio::getGrupoMuscular));
        return lista;
    }

    // maior id + 1
    private int gerarProximoId() {
        int maiorId = 0;
        for (Exercicio exercicio : repositorio.listarTodos()) {
            if (exercicio.getId() > maiorId) {
                maiorId = exercicio.getId();
            }
        }
        return maiorId + 1;
    }
}
