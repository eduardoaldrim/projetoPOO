package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Guarda que um aluno fez um treino em tal dia. É o historico,
// e é daqui que saem as estatisticas de frequencia e calorias.
public class RegistroDeTreino {
    // formato da data na tela, tipo 05/11/2026
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int id;
    private Aluno aluno;
    private Treino treino;
    private LocalDate data;
    private double caloriasEstimadas;

    // as calorias já chegam calculadas. A gente guarda o valor aqui pra que,
    // se o treino mudar depois, o historico antigo não mude junto
    public RegistroDeTreino(int id, Aluno aluno, Treino treino, LocalDate data, double caloriasEstimadas) {
        this.id = id;
        this.aluno = aluno;
        this.treino = treino;
        this.data = data;
        this.caloriasEstimadas = caloriasEstimadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Treino getTreino() {
        return treino;
    }

    public void setTreino(Treino treino) {
        this.treino = treino;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getCaloriasEstimadas() {
        return caloriasEstimadas;
    }

    public void setCaloriasEstimadas(double caloriasEstimadas) {
        this.caloriasEstimadas = caloriasEstimadas;
    }

    // cabeçalho da tabela de registros (fica junto do toString pras colunas alinharem)
    public static String cabecalhoTabela() {
        return String.format("%-4s | %-20s | %-20s | %-10s | %s",
                "ID", "Aluno", "Treino", "Data", "Calorias (kcal)");
    }

    // o registro como uma linha da tabela
    @Override
    public String toString() {
        return String.format("%-4d | %-20s | %-20s | %-10s | %.1f",
                id, aluno.getNome(), treino.getNome(), data.format(FORMATO_DATA), caloriasEstimadas);
    }
}
