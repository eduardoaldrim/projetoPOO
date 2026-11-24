package modelo;

// Exercicio de musculação, com carga (supino, agachamento, leg press...)
public class ExercicioDeForca extends Exercicio {
    private double cargaKg;
    private int series;
    private int repeticoes;
    private String equipamento;

    // os 4 primeiros vão pro construtor da classe mae, o resto é só de força
    public ExercicioDeForca(int id, String nome, String grupoMuscular, String descricao,
                            double cargaKg, int series, int repeticoes, String equipamento) {
        super(id, nome, grupoMuscular, descricao);
        this.cargaKg = cargaKg;
        this.series = series;
        this.repeticoes = repeticoes;
        this.equipamento = equipamento;
    }

    // conta simplificada: series x repetições x carga x um fator fixo (0.05)
    // não é cientifico, é só pra ter uma estimativa didatica
    @Override
    public double estimarCaloriasGastas() {
        return series * repeticoes * cargaKg * 0.05;
    }

    public double getCargaKg() {
        return cargaKg;
    }

    public void setCargaKg(double cargaKg) {
        this.cargaKg = cargaKg;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }
}
