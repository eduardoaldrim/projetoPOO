package repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// Implementação do repositorio que guarda tudo na memoria, num ArrayList.
// Como a classe é generica ela não sabe sozinha pegar o id do objeto,
// entao recebe no construtor uma função que faz isso. Ex: new RepositorioEmLista<>(Aluno::getId)
public class RepositorioEmLista<T> implements RepositorioDeDados<T> {
    private ArrayList<T> dados = new ArrayList<>();
    private Function<T, Integer> obterId;

    public RepositorioEmLista(Function<T, Integer> obterId) {
        this.obterId = obterId;
    }

    // coloca no final da lista
    @Override
    public void inserir(T objeto) {
        dados.add(objeto);
    }

    // acha a posição do id e põe o objeto novo no lugar do antigo
    @Override
    public boolean atualizar(int id, T objetoAtualizado) {
        int posicao = buscarPosicao(id);
        if (posicao == -1) {
            return false;
        }
        dados.set(posicao, objetoAtualizado);
        return true;
    }

    // acha a posição e remove da lista
    @Override
    public boolean remover(int id) {
        int posicao = buscarPosicao(id);
        if (posicao == -1) {
            return false;
        }
        dados.remove(posicao);
        return true;
    }

    @Override
    public T buscarPorId(int id) {
        int posicao = buscarPosicao(id);
        if (posicao == -1) {
            return null;
        }
        return dados.get(posicao);
    }

    // devolve uma copia da lista, assim quem chamou pode ordenar à vontade sem bagunçar a original
    @Override
    public List<T> listarTodos() {
        return new ArrayList<>(dados);
    }

    // procura em que posição da lista ta o objeto com esse id (-1 se não tiver).
    // é usado no atualizar, remover e buscarPorId, pra não repetir o mesmo for 3 vezes
    private int buscarPosicao(int id) {
        for (int i = 0; i < dados.size(); i++) {
            Integer idDoObjeto = obterId.apply(dados.get(i)); // usa a função do construtor pra pegar o id
            if (idDoObjeto != null && idDoObjeto == id) {
                return i;
            }
        }
        return -1;
    }
}
