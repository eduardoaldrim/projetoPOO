package servico;

import modelo.Aluno;
import modelo.Treino;
import repositorio.RepositorioDeDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Regras de negocio dos alunos: cadastrar, alterar, remover, buscar e ordenar.
// O repositorio vem de fora pelo construtor (injeção de dependencia), o serviço não cria ele.
public class ServicoDeAlunos {
    private RepositorioDeDados<Aluno> repositorio;

    public ServicoDeAlunos(RepositorioDeDados<Aluno> repositorio) {
        this.repositorio = repositorio;
    }

    // dá um id novo pro aluno e guarda
    public Aluno cadastrar(Aluno aluno) {
        aluno.setId(gerarProximoId());
        repositorio.inserir(aluno);
        return aluno;
    }

    public boolean alterar(Aluno alunoAtualizado) {
        return repositorio.atualizar(alunoAtualizado.getId(), alunoAtualizado);
    }

    public boolean remover(int id) {
        return repositorio.remover(id);
    }

    public Aluno buscarPorId(int id) {
        return repositorio.buscarPorId(id);
    }

    public List<Aluno> listarTodos() {
        return repositorio.listarTodos();
    }

    // busca por atributo: alunos que tem esse pedaço no nome (sem ligar pra maiuscula/minuscula)
    public List<Aluno> buscarPorNome(String nome) {
        List<Aluno> resultado = new ArrayList<>();
        for (Aluno aluno : repositorio.listarTodos()) {
            if (aluno.getNome().toLowerCase().contains(nome.toLowerCase())) {
                resultado.add(aluno);
            }
        }
        return resultado;
    }

    // ordem alfabetica
    public List<Aluno> listarOrdenadoPorNome() {
        List<Aluno> lista = repositorio.listarTodos();
        lista.sort(Comparator.comparing(Aluno::getNome));
        return lista;
    }

    // do mais novo pro mais velho
    public List<Aluno> listarOrdenadoPorIdade() {
        List<Aluno> lista = repositorio.listarTodos();
        lista.sort(Comparator.comparingInt(Aluno::getIdade));
        return lista;
    }

    // quando um treino é excluido, ele tem que sair da lista de todos os alunos que tinham ele
    public void removerTreinoDosAlunos(Treino treino) {
        for (Aluno aluno : repositorio.listarTodos()) {
            aluno.getTreinos().remove(treino);
        }
    }

    // pega o maior id que já existe e soma 1 (se não tiver ninguem, começa no 1)
    private int gerarProximoId() {
        int maiorId = 0;
        for (Aluno aluno : repositorio.listarTodos()) {
            if (aluno.getId() > maiorId) {
                maiorId = aluno.getId();
            }
        }
        return maiorId + 1;
    }
}
