package servico;

import modelo.Aluno;
import modelo.RegistroDeTreino;
import modelo.Treino;
import repositorio.RepositorioDeDados;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Regras dos treinos realizados: registrar, alterar, remover, listar
// e as estatisticas (frequencia no mes, total de calorias, alunos parados).
// Igual aos outros, recebe o repositorio pelo construtor.
public class ServicoDeRegistros {
    private RepositorioDeDados<RegistroDeTreino> repositorio;

    public ServicoDeRegistros(RepositorioDeDados<RegistroDeTreino> repositorio) {
        this.repositorio = repositorio;
    }

    // registra que o aluno fez o treino nessa data.
    // regra: o treino tem que ser um dos treinos do aluno, senão devolve null.
    // as calorias são calculadas na hora com treino.estimarCaloriasGastas() (polimorfismo)
    public RegistroDeTreino registrarTreino(Aluno aluno, Treino treino, LocalDate data) {
        if (aluno == null || treino == null || !aluno.getTreinos().contains(treino)) {
            return null;
        }
        double calorias = treino.estimarCaloriasGastas();
        RegistroDeTreino registro = new RegistroDeTreino(gerarProximoId(), aluno, treino, data, calorias);
        repositorio.inserir(registro);
        return registro;
    }

    public boolean alterar(RegistroDeTreino registroAtualizado) {
        return repositorio.atualizar(registroAtualizado.getId(), registroAtualizado);
    }

    public boolean remover(int id) {
        return repositorio.remover(id);
    }

    // apaga o historico inteiro do aluno. Chamado quando o aluno é excluido,
    // pra não ficar registro de um aluno que nem existe mais
    public void removerRegistrosDoAluno(Aluno aluno) {
        for (RegistroDeTreino registro : listarPorAluno(aluno)) {
            repositorio.remover(registro.getId());
        }
    }

    public RegistroDeTreino buscarPorId(int id) {
        return repositorio.buscarPorId(id);
    }

    public List<RegistroDeTreino> listarTodos() {
        return repositorio.listarTodos();
    }

    // busca por atributo: todos os registros de um aluno
    public List<RegistroDeTreino> listarPorAluno(Aluno aluno) {
        List<RegistroDeTreino> resultado = new ArrayList<>();
        for (RegistroDeTreino registro : repositorio.listarTodos()) {
            if (registro.getAluno().equals(aluno)) {
                resultado.add(registro);
            }
        }
        return resultado;
    }

    // do mais antigo pro mais recente
    public List<RegistroDeTreino> listarOrdenadoPorData() {
        List<RegistroDeTreino> lista = repositorio.listarTodos();
        lista.sort(Comparator.comparing(RegistroDeTreino::getData));
        return lista;
    }

    // de quem gastou mais caloria pra quem gastou menos
    public List<RegistroDeTreino> listarOrdenadoPorCalorias() {
        List<RegistroDeTreino> lista = repositorio.listarTodos();
        lista.sort(Comparator.comparingDouble(RegistroDeTreino::getCaloriasEstimadas).reversed());
        return lista;
    }

    // quantas vezes o aluno treinou num mes especifico
    public int contarFrequenciaNoMes(Aluno aluno, YearMonth mes) {
        int contador = 0;
        for (RegistroDeTreino registro : listarPorAluno(aluno)) {
            if (YearMonth.from(registro.getData()).equals(mes)) { // YearMonth.from pega só mes/ano da data
                contador++;
            }
        }
        return contador;
    }

    // frequencia no mes atual, é a coluna "Frequencia (mes)" da tabela de alunos
    public int contarFrequenciaNoMesAtual(Aluno aluno) {
        return contarFrequenciaNoMes(aluno, YearMonth.now());
    }

    // soma das calorias de todos os treinos que o aluno já fez
    public double totalDeCaloriasDoAluno(Aluno aluno) {
        double total = 0;
        for (RegistroDeTreino registro : listarPorAluno(aluno)) {
            total += registro.getCaloriasEstimadas();
        }
        return total;
    }

    // dia do ultimo treino do aluno (null se nunca treinou)
    public LocalDate dataDoUltimoTreino(Aluno aluno) {
        LocalDate ultima = null;
        for (RegistroDeTreino registro : listarPorAluno(aluno)) {
            if (ultima == null || registro.getData().isAfter(ultima)) {
                ultima = registro.getData();
            }
        }
        return ultima;
    }

    // alerta de inatividade (diferencial): alunos que não treinam faz X dias ou que nunca treinaram
    public List<Aluno> listarAlunosInativos(List<Aluno> alunos, int dias) {
        LocalDate limite = LocalDate.now().minusDays(dias);
        List<Aluno> inativos = new ArrayList<>();
        for (Aluno aluno : alunos) {
            LocalDate ultimo = dataDoUltimoTreino(aluno);
            if (ultimo == null || ultimo.isBefore(limite)) {
                inativos.add(aluno);
            }
        }
        return inativos;
    }

    // mostra os registros em forma de tabela
    public void imprimirTabela(List<RegistroDeTreino> registros) {
        if (registros.isEmpty()) {
            System.out.println("Nenhum registro encontrado.");
            return;
        }
        System.out.println(RegistroDeTreino.cabecalhoTabela());
        for (RegistroDeTreino registro : registros) {
            System.out.println(registro); // chama o toString do registro
        }
    }

    private int gerarProximoId() {
        int maiorId = 0;
        for (RegistroDeTreino registro : repositorio.listarTodos()) {
            if (registro.getId() > maiorId) {
                maiorId = registro.getId();
            }
        }
        return maiorId + 1;
    }
}
