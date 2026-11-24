package repositorio;

import java.util.List;

// Interface generica com o CRUD basico. O T é o tipo guardado (Aluno, Treino, Exercicio, RegistroDeTreino).
// Os serviços só conhecem essa interface, não a implementação. Quem escolhe a implementação
// é o Programa, que cria o repositorio e passa pro serviço pelo construtor (inversão de controle / injeção de dependencia).
// Se um dia quiser salvar em arquivo ou banco, é só criar outra classe que implementa isso.
public interface RepositorioDeDados<T> {

    // guarda um objeto novo
    void inserir(T objeto);

    // troca o objeto que tem esse id pelo atualizado. false se o id não existir
    boolean atualizar(int id, T objetoAtualizado);

    // tira o objeto com esse id. false se não achou
    boolean remover(int id);

    // acha pelo id, ou null se não tiver
    T buscarPorId(int id);

    // devolve todo mundo que ta guardado
    List<T> listarTodos();
}
