package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Aluno da academia. Cada aluno tem a lista de treinos dele (composição).
public class Aluno {
    private int id;
    private String nome;
    private int idade;
    private double pesoKg;
    private double alturaM;
    private LocalDate dataDeCadastro;

    // começa vazia, os treinos vão sendo colocados depois
    private List<Treino> treinos = new ArrayList<>();

    public Aluno(int id, LocalDate dataDeCadastro, double alturaM, double pesoKg, int idade, String nome) {
        this.id = id;
        this.dataDeCadastro = dataDeCadastro;
        this.alturaM = alturaM;
        this.pesoKg = pesoKg;
        this.idade = idade;
        this.nome = nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public void setAlturaM(double alturaM) {
        this.alturaM = alturaM;
    }

    public void setDataDeCadastro(LocalDate dataDeCadastro) {
        this.dataDeCadastro = dataDeCadastro;
    }

    public double getAlturaM() {
        return alturaM;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public LocalDate getDataDeCadastro() {
        return dataDeCadastro;
    }

    public List<Treino> getTreinos() {
        return treinos;
    }
}
