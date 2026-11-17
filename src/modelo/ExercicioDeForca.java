package modelo;

public class ExercicioDeForca {
    private Double cargaKg;
    private Integer series;
    private Integer repeticoes;
    private String equipamento;

    public ExercicioDeForca(Double cargaKg, Integer series, Integer repeticoes, String equipamento) {
        this.cargaKg = cargaKg;
        this.series = series;
        this.repeticoes = repeticoes;
        this.equipamento = equipamento;
    }

    public Double getCargaKg() {
        return cargaKg;
    }

    public void setCargaKg(Double cargaKg) {
        this.cargaKg = cargaKg;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(Integer repeticoes) {
        this.repeticoes = repeticoes;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }
}
