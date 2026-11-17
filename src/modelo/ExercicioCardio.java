package modelo;

import modelo.enumeradas.Intensidade;

public class ExercicioCardio {
    private Integer duracaoMinutos;
    private Intensidade intensidade;

    public ExercicioCardio(Integer duracaoMinutos, Intensidade intensidade) {
        this.duracaoMinutos = duracaoMinutos;
        this.intensidade = intensidade;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public Intensidade getIntensidade() {
        return intensidade;
    }

    public void setIntensidade(Intensidade intensidade) {
        this.intensidade = intensidade;
    }
}
