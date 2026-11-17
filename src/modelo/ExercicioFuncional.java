package modelo;

public class ExercicioFuncional {
    private Integer rodadas;
    private Integer tempoPorRodadaSegundos;

    public ExercicioFuncional(Integer rodadas, Integer tempoPorRodadaSegundos) {
        this.rodadas = rodadas;
        this.tempoPorRodadaSegundos = tempoPorRodadaSegundos;
    }

    public Integer getRodadas() {
        return rodadas;
    }

    public void setRodadas(Integer rodadas) {
        this.rodadas = rodadas;
    }

    public Integer getTempoPorRodadaSegundos() {
        return tempoPorRodadaSegundos;
    }

    public void setTempoPorRodadaSegundos(Integer tempoPorRodadaSegundos) {
        this.tempoPorRodadaSegundos = tempoPorRodadaSegundos;
    }
}
