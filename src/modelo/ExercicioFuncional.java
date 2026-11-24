package modelo;

// Exercicio funcional / circuito: burpee, kettlebell, box jump...
public class ExercicioFuncional extends Exercicio {
    private int rodadas;
    private int tempoPorRodadaSegundos;

    public ExercicioFuncional(int id, String nome, String grupoMuscular, String descricao,
                              int rodadas, int tempoPorRodadaSegundos) {
        super(id, nome, grupoMuscular, descricao);
        this.rodadas = rodadas;
        this.tempoPorRodadaSegundos = tempoPorRodadaSegundos;
    }

    // calorias proporcionais ao tempo total: rodadas x minutos de cada rodada x 10 kcal por minuto
    @Override
    public double estimarCaloriasGastas() {
        return rodadas * (tempoPorRodadaSegundos / 60.0) * 10;
    }

    public int getRodadas() {
        return rodadas;
    }

    public void setRodadas(int rodadas) {
        this.rodadas = rodadas;
    }

    public int getTempoPorRodadaSegundos() {
        return tempoPorRodadaSegundos;
    }

    public void setTempoPorRodadaSegundos(int tempoPorRodadaSegundos) {
        this.tempoPorRodadaSegundos = tempoPorRodadaSegundos;
    }
}
