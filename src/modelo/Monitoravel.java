package modelo;

// Interface pra tudo que dá pra estimar quanto de caloria gasta.
// Quem implementa: Exercicio (e as 3 filhas, por herança) e o Treino.
// O Treino usa isso pra somar as calorias sem precisar saber o tipo de cada exercicio.
public interface Monitoravel {

    // devolve a estimativa de calorias gastas (em kcal)
    double estimarCaloriasGastas();
}
