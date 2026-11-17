package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Aluno {
    private Integer id;
    private String nome;
    private Integer idade;
    private Double pesoKg;
    private Double alturaM;
    private LocalDate dataDeCadastro;

    private ArrayList <Treino> treinos = new ArrayList<>();

    public Aluno(Integer id, LocalDate dataDeCadastro, Double alturaM, Double pesoKg, Integer idade, String nome) {
        this.id = id;
        this.dataDeCadastro = dataDeCadastro;
        this.alturaM = alturaM;
        this.pesoKg = pesoKg;
        this.idade = idade;
        this.nome = nome;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public void setDataDeCadastro(LocalDate dataDeCadastro) {
        this.dataDeCadastro = dataDeCadastro;
    }

    public Double getAlturaM() {
        return alturaM;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public Double getPesoKg() {
        return pesoKg;
    }

    public LocalDate getDataDeCadastro() {
        return dataDeCadastro;
    }

    public ArrayList<Treino> getTreinos() {
        return treinos;
    }
}
