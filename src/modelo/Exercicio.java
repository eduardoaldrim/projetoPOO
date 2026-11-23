package modelo;

// Classe base de todos os exercicios. É abstrata, entao ninguem faz "new Exercicio()",
// tem que criar um de força, cardio ou funcional.
// Ela implementa Monitoravel mas deixa o calculo das calorias pra cada filha fazer do seu jeito.
public abstract class Exercicio implements Monitoravel {
    private int id;
    private String nome;
    private String grupoMuscular;
    private String descricao;

    // protected pq só as classes filhas chamam esse construtor (pelo super)
    protected Exercicio(int id, String nome, String grupoMuscular, String descricao) {
        this.id = id;
        this.nome = nome;
        this.grupoMuscular = grupoMuscular;
        this.descricao = descricao;
    }

    // cada tipo de exercicio é obrigado a implementar a sua conta
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

    // cabeçalho da tabela de exercicios, fica aqui junto do toString pra coluna bater certinho
    public static String cabecalhoTabela() {
        return String.format("%-4s | %-34s | %-14s | %-18s | %s",
                "ID", "Nome", "Grupo muscular", "Tipo", "Calorias (kcal)");
    }

    // uma linha da tabela. O estimarCaloriasGastas() aqui chama o metodo da classe filha (polimorfismo)
    @Override
    public String toString() {
        return String.format("%-4d | %-34s | %-14s | %-18s | %.1f",
                id, nome, grupoMuscular, getClass().getSimpleName(), estimarCaloriasGastas());
    }
}
