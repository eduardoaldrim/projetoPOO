package modelo;

import modelo.enumeradas.Objetivo;

import java.util.ArrayList;
import java.util.List;

// Um treino é um conjunto de exercicios feitos numa sessão (ex: "Peito e triceps" na segunda).
// Composição: o treino tem a lista de exercicios dele.
public class Treino implements Monitoravel {
    private int id;
    private String nome;
    private Objetivo objetivo;
    private String diaDaSemana;
    private List<Exercicio> exercicios = new ArrayList<>();

    public Treino(int id, String nome, Objetivo objetivo, String diaDaSemana) {
        this.id = id;
        this.nome = nome;
        this.objetivo = objetivo;
        this.diaDaSemana = diaDaSemana;
    }

    // soma as calorias de todos os exercicios do treino.
    // aqui é o polimorfismo mais claro: o treino nem sabe se é força, cardio ou funcional,
    // ele só chama estimarCaloriasGastas() e cada um responde do seu jeito
    @Override
    public double estimarCaloriasGastas() {
        double total = 0;
        for (Exercicio exercicio : exercicios) {
            total += exercicio.estimarCaloriasGastas();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public String getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(String diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public List<Exercicio> getExercicios() {
        return exercicios;
    }

    // cabeçalho da tabela de treinos
    public static String cabecalhoTabela() {
        return String.format("%-4s | %-20s | %-15s | %-8s | %-10s | %s",
                "ID", "Nome", "Objetivo", "Dia", "Exercicios", "Calorias (kcal)");
    }

    // linha da tabela com os dados do treino
    @Override
    public String toString() {
        return String.format("%-4d | %-20s | %-15s | %-8s | %-10d | %.1f",
                id, nome, objetivo, diaDaSemana, exercicios.size(), estimarCaloriasGastas());
    }
}
