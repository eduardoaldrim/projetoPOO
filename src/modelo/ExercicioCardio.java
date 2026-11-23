package modelo;

import modelo.enumeradas.Intensidade;

// Exercicio aerobico: esteira, bike, eliptico, corda...
public class ExercicioCardio extends Exercicio {
    private int duracaoMinutos;
    private Intensidade intensidade;

    public ExercicioCardio(int id, String nome, String grupoMuscular, String descricao,
                           int duracaoMinutos, Intensidade intensidade) {
        super(id, nome, grupoMuscular, descricao);
        this.duracaoMinutos = duracaoMinutos;
        this.intensidade = intensidade;
    }

    // formula do MET: MET x peso x horas
    // como o exercicio não sabe quem vai fazer, usamos um peso de referencia de 70kg
    @Override
    public double estimarCaloriasGastas() {
        // cada intensidade tem um MET diferente
        double met = switch (intensidade) {
            case BAIXA -> 4.0;
            case MEDIA -> 7.0;
            case ALTA -> 10.0;
        };
        return met * 70 * (duracaoMinutos / 60.0); // divide por 60 pra passar minutos pra horas
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(int duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public Intensidade getIntensidade() {
        return intensidade;
    }

    public void setIntensidade(Intensidade intensidade) {
        this.intensidade = intensidade;
    }
}
