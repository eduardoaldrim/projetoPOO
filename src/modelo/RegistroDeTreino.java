package modelo;

import java.time.LocalDate;

public class RegistroDeTreino {
    private Integer id;
    private Aluno aluno;
    private Treino treino;
    private LocalDate data;
    private Double caloriasEstimadas;

    public RegistroDeTreino(Integer id, Aluno aluno, Treino treino, LocalDate data, Double caloriasEstimadas) {
        this.id = id;
        this.aluno = aluno;
        this.treino = treino;
        this.data = data;
        this.caloriasEstimadas = caloriasEstimadas;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Treino getTreino() {
        return treino;
    }

    public LocalDate getData() {
        return data;
    }

    public Double getCaloriasEstimadas() {
        return caloriasEstimadas;
    }
}
