package modelo;

public abstract class Exercicio implements Monitoravel {

    private int id;
    private String nome;
    private String grupoMuscular;
    private String descricao;

    protected Exercicio(int id, String nome, String grupoMuscular, String descricao) {
        this.id = id;
        this.nome = nome;
        this.grupoMuscular = grupoMuscular;
        this.descricao = descricao;
    }

    @Override
    public abstract double estimarCaloriasGastas();

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

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %s | %.1f kcal",
                id, nome, grupoMuscular, getClass().getSimpleName(), estimarCaloriasGastas());
    }
}
