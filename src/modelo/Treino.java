package modelo;

import modelo.enumeradas.Objetivo;

import java.util.ArrayList;

public class Treino implements Monitoravel {
    private Integer id;
    private String nome;
    private Objetivo objetivo;
    private String diaDaSemana;
    private ArrayList <Exercicio> exercicios = new ArrayList<>();

    public Treino(Integer id, String nome, Objetivo objetivo, String diaDaSemana) {
        this.id = id;
        this.nome = nome;
        this.objetivo = objetivo;
        this.diaDaSemana = diaDaSemana;
    }

    @Override
    public double estimarCaloriasGastas() {
        return 0;
    }
}
