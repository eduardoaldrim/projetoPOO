package aplicacao;

import modelo.Aluno;
import modelo.Exercicio;
import modelo.ExercicioCardio;
import modelo.ExercicioDeForca;
import modelo.ExercicioFuncional;
import modelo.RegistroDeTreino;
import modelo.Treino;
import modelo.enumeradas.Intensidade;
import modelo.enumeradas.Objetivo;
import repositorio.RepositorioDeDados;
import repositorio.RepositorioEmLista;
import servico.ServicoDeAlunos;
import servico.ServicoDeExercicios;
import servico.ServicoDeRegistros;
import servico.ServicoDeTreinos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/*
 * GERENCIADOR DE TREINOS DE ACADEMIA
 *
 * Sistema de terminal pra uma academia: cadastra alunos, exercicios e treinos,
 * e registra os treinos que cada aluno fez. Com os registros dá pra ver a frequencia
 * do aluno no mes, quantas calorias ele gastou e quem ta sumido da academia.
 *
 * Essa é a classe principal. Ela cria os repositorios, entrega pros serviços
 * (inversão de controle / injeção de dependencia), cadastra os dados iniciais e mostra os menus.
 */
public class Programa {
    // um Scanner só pro programa todo, lendo o que a pessoa digita
    private static Scanner entrada = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // os serviços ficam aqui pra todos os menus conseguirem usar
    private static ServicoDeAlunos servicoDeAlunos;
    private static ServicoDeExercicios servicoDeExercicios;
    private static ServicoDeTreinos servicoDeTreinos;
    private static ServicoDeRegistros servicoDeRegistros;

    public static void main(String[] args) {
        // primeiro cria os repositorios. Cada um recebe como pegar o id do objeto que ele guarda
        RepositorioDeDados<Aluno> repositorioAlunos = new RepositorioEmLista<>(Aluno::getId);
        RepositorioDeDados<Exercicio> repositorioExercicios = new RepositorioEmLista<>(Exercicio::getId);
        RepositorioDeDados<Treino> repositorioTreinos = new RepositorioEmLista<>(Treino::getId);
        RepositorioDeDados<RegistroDeTreino> repositorioRegistros = new RepositorioEmLista<>(RegistroDeTreino::getId);

        // aqui é a injeção de dependencia: o Programa cria o repositorio e passa pro serviço.
        // o serviço nem sabe que por tras é um ArrayList
        servicoDeAlunos = new ServicoDeAlunos(repositorioAlunos);
        servicoDeExercicios = new ServicoDeExercicios(repositorioExercicios);
        servicoDeTreinos = new ServicoDeTreinos(repositorioTreinos, repositorioExercicios);
        servicoDeRegistros = new ServicoDeRegistros(repositorioRegistros);

        carregarDadosIniciais();

        // menu principal, fica rodando até escolher Sair
        int opcao;
        do {
            System.out.println();
            System.out.println("===== GERENCIADOR DE TREINOS =====");
            System.out.println("1. Alunos");
            System.out.println("2. Exercicios");
            System.out.println("3. Treinos");
            System.out.println("4. Registrar treino realizado");
            System.out.println("5. Sair");
            opcao = lerInteiro("Opção", 1, 5, null);

            switch (opcao) {
                case 1 -> menuAlunos();
                case 2 -> menuExercicios();
                case 3 -> menuTreinos();
                case 4 -> menuRegistros();
                case 5 -> System.out.println("Saindo... até a proxima!");
            }
        } while (opcao != 5);
    }

    // ==================== ALUNOS ====================

    // submenu de alunos
    private static void menuAlunos() {
        int opcao;
        do {
            System.out.println();
            System.out.println("----- ALUNOS -----");
            imprimirSubmenu();
            opcao = lerInteiro("Opção", 0, 6, null);

            switch (opcao) {
                case 1 -> incluirAluno();
                case 2 -> alterarAluno();
                case 3 -> excluirAluno();
                case 4 -> imprimirAlunos(servicoDeAlunos.listarTodos());
                case 5 -> imprimirAlunos(servicoDeAlunos.buscarPorNome(lerTexto("Nome (ou parte dele)", null)));
                case 6 -> ordenarAlunos();
            }
        } while (opcao != 0);
    }

    private static void incluirAluno() {
        String nome = lerTexto("Nome", null);
        int idade = lerInteiro("Idade", 1, 120, null); // não deixa idade negativa nem zero
        double peso = lerDouble("Peso (kg)", 1, null);
        double altura = lerDouble("Altura (m)", 0.5, null);

        Aluno aluno = new Aluno(0, LocalDate.now(), altura, peso, idade, nome); // o id certo vem do serviço
        escolherTreinosDoAluno(aluno);
        servicoDeAlunos.cadastrar(aluno);
        System.out.println("Aluno cadastrado com id " + aluno.getId() + ".");
    }

    // mostra o valor atual entre colchetes, se apertar enter mantem o que já tava
    private static void alterarAluno() {
        Aluno aluno = servicoDeAlunos.buscarPorId(lerInteiro("ID do aluno", 1, Integer.MAX_VALUE, null));
        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }
        System.out.println("(aperte enter pra manter o valor atual)");
        aluno.setNome(lerTexto("Nome", aluno.getNome()));
        aluno.setIdade(lerInteiro("Idade", 1, 120, aluno.getIdade()));
        aluno.setPesoKg(lerDouble("Peso (kg)", 1, aluno.getPesoKg()));
        aluno.setAlturaM(lerDouble("Altura (m)", 0.5, aluno.getAlturaM()));
        escolherTreinosDoAluno(aluno);
        servicoDeAlunos.alterar(aluno);
        System.out.println("Aluno alterado.");
    }

    private static void excluirAluno() {
        Aluno aluno = servicoDeAlunos.buscarPorId(lerInteiro("ID do aluno", 1, Integer.MAX_VALUE, null));
        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }
        if (confirmar("Excluir " + aluno.getNome() + " e todo o historico dele?")) {
            servicoDeRegistros.removerRegistrosDoAluno(aluno); // apaga o historico junto
            servicoDeAlunos.remover(aluno.getId());
            System.out.println("Aluno excluido.");
        }
    }

    // pergunta quais treinos o aluno vai fazer. Enter em branco deixa como ta
    private static void escolherTreinosDoAluno(Aluno aluno) {
        System.out.println("Treinos disponiveis:");
        imprimirTreinos(servicoDeTreinos.listarTodos());
        List<Integer> ids = lerIds("IDs dos treinos separados por virgula (enter = pular)");
        if (ids == null) {
            return;
        }
        aluno.getTreinos().clear();
        for (int id : ids) {
            Treino treino = servicoDeTreinos.buscarPorId(id);
            if (treino == null) {
                System.out.println("Treino " + id + " não existe, ignorei.");
            } else if (!aluno.getTreinos().contains(treino)) { // evita colocar o mesmo treino 2 vezes
                aluno.getTreinos().add(treino);
            }
        }
    }

    private static void ordenarAlunos() {
        System.out.println("1. Por nome");
        System.out.println("2. Por idade");
        System.out.println("3. Por frequencia no mes");
        int opcao = lerInteiro("Ordenar", 1, 3, null);
        if (opcao == 1) {
            imprimirAlunos(servicoDeAlunos.listarOrdenadoPorNome());
        } else if (opcao == 2) {
            imprimirAlunos(servicoDeAlunos.listarOrdenadoPorIdade());
        } else {
            // a frequencia vem dos registros, entao a ordenação usa o serviço de registros. Quem treinou mais vem primeiro
            List<Aluno> lista = servicoDeAlunos.listarTodos();
            lista.sort(Comparator.comparingInt((Aluno a) -> servicoDeRegistros.contarFrequenciaNoMesAtual(a)).reversed());
            imprimirAlunos(lista);
        }
    }

    // tabela de alunos. A frequencia é calculada na hora a partir dos registros do mes atual
    private static void imprimirAlunos(List<Aluno> alunos) {
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno encontrado.");
            return;
        }
        System.out.println(String.format("%-4s | %-16s | %-5s | %-9s | %-10s | %-7s | %s",
                "ID", "Nome", "Idade", "Peso (kg)", "Altura (m)", "Treinos", "Frequencia (mes)"));
        for (Aluno aluno : alunos) {
            System.out.println(String.format("%-4d | %-16s | %-5d | %-9.1f | %-10.2f | %-7d | %d",
                    aluno.getId(), aluno.getNome(), aluno.getIdade(), aluno.getPesoKg(), aluno.getAlturaM(),
                    aluno.getTreinos().size(), servicoDeRegistros.contarFrequenciaNoMesAtual(aluno)));
        }
    }

    // ==================== EXERCICIOS ====================

    private static void menuExercicios() {
        int opcao;
        do {
            System.out.println();
            System.out.println("----- EXERCICIOS -----");
            imprimirSubmenu();
            opcao = lerInteiro("Opção", 0, 6, null);

            switch (opcao) {
                case 1 -> incluirExercicio();
                case 2 -> alterarExercicio();
                case 3 -> excluirExercicio();
                case 4 -> imprimirExercicios(servicoDeExercicios.listarTodos());
                case 5 -> imprimirExercicios(servicoDeExercicios.buscarPorGrupoMuscular(lerTexto("Grupo muscular", null)));
                case 6 -> ordenarExercicios();
            }
        } while (opcao != 0);
    }

    private static void incluirExercicio() {
        System.out.println("1. Força");
        System.out.println("2. Cardio");
        System.out.println("3. Funcional");
        int tipo = lerInteiro("Tipo", 1, 3, null);

        // esses campos são comuns aos 3 tipos (vem da classe Exercicio)
        String nome = lerTexto("Nome", null);
        String grupo = lerTexto("Grupo muscular", null);
        String descricao = lerTexto("Descrição", null);

        // aqui cria o tipo certo. A variavel é do tipo Exercicio mas o objeto é da classe filha
        Exercicio exercicio;
        if (tipo == 1) {
            double carga = lerDouble("Carga (kg)", 0, null);
            int series = lerInteiro("Series", 1, 50, null);
            int repeticoes = lerInteiro("Repetições", 1, 500, null);
            String equipamento = lerTexto("Equipamento", null);
            exercicio = new ExercicioDeForca(0, nome, grupo, descricao, carga, series, repeticoes, equipamento);
        } else if (tipo == 2) {
            int duracao = lerInteiro("Duração (minutos)", 1, 600, null);
            Intensidade intensidade = lerIntensidade(null);
            exercicio = new ExercicioCardio(0, nome, grupo, descricao, duracao, intensidade);
        } else {
            int rodadas = lerInteiro("Rodadas", 1, 100, null);
            int tempo = lerInteiro("Tempo por rodada (segundos)", 1, 3600, null);
            exercicio = new ExercicioFuncional(0, nome, grupo, descricao, rodadas, tempo);
        }
        servicoDeExercicios.cadastrar(exercicio);
        System.out.println("Exercicio cadastrado com id " + exercicio.getId() + ".");
    }

    private static void alterarExercicio() {
        Exercicio exercicio = servicoDeExercicios.buscarPorId(lerInteiro("ID do exercicio", 1, Integer.MAX_VALUE, null));
        if (exercicio == null) {
            System.out.println("Exercicio não encontrado.");
            return;
        }
        System.out.println("(aperte enter pra manter o valor atual)");
        exercicio.setNome(lerTexto("Nome", exercicio.getNome()));
        exercicio.setGrupoMuscular(lerTexto("Grupo muscular", exercicio.getGrupoMuscular()));
        exercicio.setDescricao(lerTexto("Descrição", exercicio.getDescricao()));

        // instanceof pra saber de que tipo é e pedir os campos especificos dele
        if (exercicio instanceof ExercicioDeForca forca) {
            forca.setCargaKg(lerDouble("Carga (kg)", 0, forca.getCargaKg()));
            forca.setSeries(lerInteiro("Series", 1, 50, forca.getSeries()));
            forca.setRepeticoes(lerInteiro("Repetições", 1, 500, forca.getRepeticoes()));
            forca.setEquipamento(lerTexto("Equipamento", forca.getEquipamento()));
        } else if (exercicio instanceof ExercicioCardio cardio) {
            cardio.setDuracaoMinutos(lerInteiro("Duração (minutos)", 1, 600, cardio.getDuracaoMinutos()));
            cardio.setIntensidade(lerIntensidade(cardio.getIntensidade()));
        } else if (exercicio instanceof ExercicioFuncional funcional) {
            funcional.setRodadas(lerInteiro("Rodadas", 1, 100, funcional.getRodadas()));
            funcional.setTempoPorRodadaSegundos(lerInteiro("Tempo por rodada (segundos)", 1, 3600, funcional.getTempoPorRodadaSegundos()));
        }
        servicoDeExercicios.alterar(exercicio);
        System.out.println("Exercicio alterado.");
    }

    private static void excluirExercicio() {
        Exercicio exercicio = servicoDeExercicios.buscarPorId(lerInteiro("ID do exercicio", 1, Integer.MAX_VALUE, null));
        if (exercicio == null) {
            System.out.println("Exercicio não encontrado.");
            return;
        }
        if (confirmar("Excluir " + exercicio.getNome() + "? Ele sai de todos os treinos também.")) {
            servicoDeTreinos.removerExercicioDosTreinos(exercicio);
            servicoDeExercicios.remover(exercicio.getId());
            System.out.println("Exercicio excluido.");
        }
    }

    private static void ordenarExercicios() {
        System.out.println("1. Por nome");
        System.out.println("2. Por grupo muscular");
        if (lerInteiro("Ordenar", 1, 2, null) == 1) {
            imprimirExercicios(servicoDeExercicios.listarOrdenadoPorNome());
        } else {
            imprimirExercicios(servicoDeExercicios.listarOrdenadoPorGrupoMuscular());
        }
    }

    private static void imprimirExercicios(List<Exercicio> exercicios) {
        if (exercicios.isEmpty()) {
            System.out.println("Nenhum exercicio encontrado.");
            return;
        }
        System.out.println(Exercicio.cabecalhoTabela());
        for (Exercicio exercicio : exercicios) {
            System.out.println(exercicio);
        }
    }

    // ==================== TREINOS ====================

    private static void menuTreinos() {
        int opcao;
        do {
            System.out.println();
            System.out.println("----- TREINOS -----");
            imprimirSubmenu();
            opcao = lerInteiro("Opção", 0, 6, null);

            switch (opcao) {
                case 1 -> incluirTreino();
                case 2 -> alterarTreino();
                case 3 -> excluirTreino();
                case 4 -> imprimirTreinos(servicoDeTreinos.listarTodos());
                case 5 -> imprimirTreinos(servicoDeTreinos.buscarPorObjetivo(lerObjetivo(null)));
                case 6 -> ordenarTreinos();
            }
        } while (opcao != 0);
    }

    private static void incluirTreino() {
        String nome = lerTexto("Nome", null);
        Objetivo objetivo = lerObjetivo(null);
        String dia = lerTexto("Dia da semana", null);

        Treino treino = new Treino(0, nome, objetivo, dia);
        escolherExerciciosDoTreino(treino, true); // treino novo não pode ficar sem exercicio
        servicoDeTreinos.cadastrar(treino);
        System.out.println("Treino cadastrado com id " + treino.getId() + ".");
    }

    private static void alterarTreino() {
        Treino treino = servicoDeTreinos.buscarPorId(lerInteiro("ID do treino", 1, Integer.MAX_VALUE, null));
        if (treino == null) {
            System.out.println("Treino não encontrado.");
            return;
        }
        System.out.println("(aperte enter pra manter o valor atual)");
        treino.setNome(lerTexto("Nome", treino.getNome()));
        treino.setObjetivo(lerObjetivo(treino.getObjetivo()));
        treino.setDiaDaSemana(lerTexto("Dia da semana", treino.getDiaDaSemana()));
        escolherExerciciosDoTreino(treino, treino.getExercicios().isEmpty());
        servicoDeTreinos.alterar(treino);
        System.out.println("Treino alterado.");
    }

    private static void excluirTreino() {
        Treino treino = servicoDeTreinos.buscarPorId(lerInteiro("ID do treino", 1, Integer.MAX_VALUE, null));
        if (treino == null) {
            System.out.println("Treino não encontrado.");
            return;
        }
        if (confirmar("Excluir " + treino.getNome() + "? Ele sai da lista dos alunos.")) {
            // os registros antigos continuam, porque são historico do que já foi feito
            servicoDeAlunos.removerTreinoDosAlunos(treino);
            servicoDeTreinos.remover(treino.getId());
            System.out.println("Treino excluido.");
        }
    }

    // monta a lista de exercicios do treino a partir dos ids digitados.
    // se "obrigatorio" for true, fica pedindo até ter pelo menos um exercicio valido (validação de entrada)
    private static void escolherExerciciosDoTreino(Treino treino, boolean obrigatorio) {
        System.out.println("Exercicios disponiveis:");
        imprimirExercicios(servicoDeExercicios.listarTodos());
        while (true) {
            List<Integer> ids = lerIds("IDs dos exercicios separados por virgula" + (obrigatorio ? "" : " (enter = manter)"));
            if (ids == null) {
                if (!obrigatorio) {
                    return;
                }
                System.out.println("O treino precisa ter pelo menos um exercicio.");
                continue;
            }
            // guarda os antigos, caso nenhum id digitado preste
            List<Exercicio> antigos = new ArrayList<>(treino.getExercicios());
            treino.getExercicios().clear();
            for (int id : ids) {
                if (!servicoDeTreinos.adicionarExercicio(treino, id)) {
                    System.out.println("Exercicio " + id + " não existe, ignorei.");
                }
            }
            if (!treino.getExercicios().isEmpty()) {
                return;
            }
            treino.getExercicios().addAll(antigos);
            System.out.println("Nenhum id valido.");
            if (!obrigatorio) {
                return;
            }
        }
    }

    private static void ordenarTreinos() {
        System.out.println("1. Por nome");
        System.out.println("2. Por objetivo");
        if (lerInteiro("Ordenar", 1, 2, null) == 1) {
            imprimirTreinos(servicoDeTreinos.listarOrdenadoPorNome());
        } else {
            imprimirTreinos(servicoDeTreinos.listarOrdenadoPorObjetivo());
        }
    }

    private static void imprimirTreinos(List<Treino> treinos) {
        if (treinos.isEmpty()) {
            System.out.println("Nenhum treino encontrado.");
            return;
        }
        System.out.println(Treino.cabecalhoTabela());
        for (Treino treino : treinos) {
            System.out.println(treino);
        }
    }

    // ==================== REGISTROS (treinos realizados) ====================

    private static void menuRegistros() {
        int opcao;
        do {
            System.out.println();
            System.out.println("----- TREINOS REALIZADOS -----");
            System.out.println("1. Registrar treino realizado");
            System.out.println("2. Alterar data de um registro");
            System.out.println("3. Excluir registro");
            System.out.println("4. Listar");
            System.out.println("5. Buscar por aluno");
            System.out.println("6. Ordenar listagem");
            System.out.println("7. Alunos sem treinar (alerta de inatividade)");
            System.out.println("0. Voltar");
            opcao = lerInteiro("Opção", 0, 7, null);

            switch (opcao) {
                case 1 -> registrarTreino();
                case 2 -> alterarRegistro();
                case 3 -> excluirRegistro();
                case 4 -> servicoDeRegistros.imprimirTabela(servicoDeRegistros.listarTodos());
                case 5 -> buscarRegistrosDoAluno();
                case 6 -> ordenarRegistros();
                case 7 -> mostrarAlunosInativos();
            }
        } while (opcao != 0);
    }

    // escolhe o aluno, depois um dos treinos DELE, e a data. As calorias o serviço calcula sozinho
    private static void registrarTreino() {
        imprimirAlunos(servicoDeAlunos.listarTodos());
        Aluno aluno = servicoDeAlunos.buscarPorId(lerInteiro("ID do aluno", 1, Integer.MAX_VALUE, null));
        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }
        if (aluno.getTreinos().isEmpty()) {
            System.out.println("Esse aluno ainda não tem treino. Coloque um treino nele no menu de alunos.");
            return;
        }
        imprimirTreinos(aluno.getTreinos());
        Treino treino = servicoDeTreinos.buscarPorId(lerInteiro("ID do treino", 1, Integer.MAX_VALUE, null));
        LocalDate data = lerData("Data (dd/mm/aaaa)", LocalDate.now()); // enter = hoje

        RegistroDeTreino registro = servicoDeRegistros.registrarTreino(aluno, treino, data);
        if (registro == null) {
            System.out.println("Esse treino não é do aluno.");
        } else {
            System.out.println(String.format("Treino registrado! Calorias estimadas: %.1f kcal", registro.getCaloriasEstimadas()));
        }
    }

    private static void alterarRegistro() {
        RegistroDeTreino registro = servicoDeRegistros.buscarPorId(lerInteiro("ID do registro", 1, Integer.MAX_VALUE, null));
        if (registro == null) {
            System.out.println("Registro não encontrado.");
            return;
        }
        registro.setData(lerData("Nova data (dd/mm/aaaa)", registro.getData()));
        servicoDeRegistros.alterar(registro);
        System.out.println("Registro alterado.");
    }

    private static void excluirRegistro() {
        int id = lerInteiro("ID do registro", 1, Integer.MAX_VALUE, null);
        if (servicoDeRegistros.remover(id)) {
            System.out.println("Registro excluido.");
        } else {
            System.out.println("Registro não encontrado.");
        }
    }

    // mostra o historico do aluno e um resuminho com as estatisticas dele
    private static void buscarRegistrosDoAluno() {
        Aluno aluno = servicoDeAlunos.buscarPorId(lerInteiro("ID do aluno", 1, Integer.MAX_VALUE, null));
        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }
        servicoDeRegistros.imprimirTabela(servicoDeRegistros.listarPorAluno(aluno));
        LocalDate ultimo = servicoDeRegistros.dataDoUltimoTreino(aluno);
        System.out.println("Treinos no mes: " + servicoDeRegistros.contarFrequenciaNoMesAtual(aluno));
        System.out.println(String.format("Total de calorias: %.1f kcal", servicoDeRegistros.totalDeCaloriasDoAluno(aluno)));
        System.out.println("Ultimo treino: " + (ultimo == null ? "nunca treinou" : ultimo.format(FORMATO_DATA)));
    }

    private static void ordenarRegistros() {
        System.out.println("1. Por data");
        System.out.println("2. Por calorias (maior primeiro)");
        if (lerInteiro("Ordenar", 1, 2, null) == 1) {
            servicoDeRegistros.imprimirTabela(servicoDeRegistros.listarOrdenadoPorData());
        } else {
            servicoDeRegistros.imprimirTabela(servicoDeRegistros.listarOrdenadoPorCalorias());
        }
    }

    private static void mostrarAlunosInativos() {
        int dias = lerInteiro("Quantos dias sem treinar", 1, 365, 7); // enter = 7 dias
        System.out.println("Alunos sem treinar nos ultimos " + dias + " dias:");
        imprimirAlunos(servicoDeRegistros.listarAlunosInativos(servicoDeAlunos.listarTodos(), dias));
    }

    // ==================== LEITURA DO TECLADO ====================
    // Todos esses metodos recebem um "valorAtual". Se a pessoa apertar enter sem digitar nada,
    // volta o valorAtual (é assim que funciona o "manter" no alterar). Se for null, obriga a digitar.

    // opções que se repetem nos menus de alunos, exercicios e treinos
    private static void imprimirSubmenu() {
        System.out.println("1. Incluir");
        System.out.println("2. Alterar");
        System.out.println("3. Excluir");
        System.out.println("4. Listar");
        System.out.println("5. Buscar");
        System.out.println("6. Ordenar listagem");
        System.out.println("0. Voltar");
    }

    // monta o texto da pergunta, tipo "Nome [Ana Souza]: "
    private static String perguntar(String mensagem, Object valorAtual) {
        System.out.print(mensagem + (valorAtual == null ? "" : " [" + valorAtual + "]") + ": ");
        return entrada.nextLine().trim();
    }

    private static String lerTexto(String mensagem, String valorAtual) {
        while (true) {
            String texto = perguntar(mensagem, valorAtual);
            if (!texto.isEmpty()) {
                return texto;
            }
            if (valorAtual != null) {
                return valorAtual;
            }
            System.out.println("Não pode ficar em branco.");
        }
    }

    // lê um numero inteiro entre minimo e maximo. Se digitar letra ou fora da faixa, pergunta de novo
    private static int lerInteiro(String mensagem, int minimo, int maximo, Integer valorAtual) {
        while (true) {
            String texto = perguntar(mensagem, valorAtual);
            if (texto.isEmpty() && valorAtual != null) {
                return valorAtual;
            }
            try {
                int numero = Integer.parseInt(texto);
                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }
                System.out.println("Digite um numero entre " + minimo + " e " + maximo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Isso não é um numero valido.");
            }
        }
    }

    // igual ao de cima mas pra numero com virgula. Aceita tanto 62,5 quanto 62.5
    private static double lerDouble(String mensagem, double minimo, Double valorAtual) {
        while (true) {
            String texto = perguntar(mensagem, valorAtual);
            if (texto.isEmpty() && valorAtual != null) {
                return valorAtual;
            }
            try {
                double numero = Double.parseDouble(texto.replace(',', '.'));
                if (numero >= minimo) {
                    return numero;
                }
                System.out.println("O valor minimo é " + minimo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Isso não é um numero valido.");
            }
        }
    }

    // lê uma data no formato dd/mm/aaaa. Não deixa colocar data no futuro
    private static LocalDate lerData(String mensagem, LocalDate valorAtual) {
        while (true) {
            String texto = perguntar(mensagem, valorAtual == null ? null : valorAtual.format(FORMATO_DATA));
            if (texto.isEmpty() && valorAtual != null) {
                return valorAtual;
            }
            try {
                LocalDate data = LocalDate.parse(texto, FORMATO_DATA);
                if (!data.isAfter(LocalDate.now())) {
                    return data;
                }
                System.out.println("A data não pode ser no futuro.");
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida, use dd/mm/aaaa.");
            }
        }
    }

    // mostra as opções do enum numeradas e devolve a escolhida
    private static Objetivo lerObjetivo(Objetivo valorAtual) {
        Objetivo[] opcoes = Objetivo.values();
        for (int i = 0; i < opcoes.length; i++) {
            System.out.println((i + 1) + ". " + opcoes[i]);
        }
        int escolha = lerInteiro("Objetivo", 1, opcoes.length, valorAtual == null ? null : valorAtual.ordinal() + 1);
        return opcoes[escolha - 1];
    }

    private static Intensidade lerIntensidade(Intensidade valorAtual) {
        Intensidade[] opcoes = Intensidade.values();
        for (int i = 0; i < opcoes.length; i++) {
            System.out.println((i + 1) + ". " + opcoes[i]);
        }
        int escolha = lerInteiro("Intensidade", 1, opcoes.length, valorAtual == null ? null : valorAtual.ordinal() + 1);
        return opcoes[escolha - 1];
    }

    // lê uma lista de ids tipo "1, 4, 7". Devolve null se a pessoa só apertou enter
    private static List<Integer> lerIds(String mensagem) {
        while (true) {
            String texto = perguntar(mensagem, null);
            if (texto.isEmpty()) {
                return null;
            }
            try {
                List<Integer> ids = new ArrayList<>();
                for (String parte : texto.split(",")) {
                    ids.add(Integer.parseInt(parte.trim()));
                }
                return ids;
            } catch (NumberFormatException e) {
                System.out.println("Use só numeros separados por virgula, tipo 1, 3, 5");
            }
        }
    }

    // pergunta sim ou não antes de apagar alguma coisa
    private static boolean confirmar(String mensagem) {
        return perguntar(mensagem + " (s/n)", null).equalsIgnoreCase("s");
    }

    // ==================== DADOS INICIAIS ====================

    // cadastra 7 objetos de cada classe quando o programa abre (requisito 8),
    // assim já da pra testar tudo sem ter que digitar um monte de coisa
    private static void carregarDadosIniciais() {
        // exercicios de força (ids 1 a 7)
        servicoDeExercicios.cadastrar(new ExercicioDeForca(0, "Supino reto", "Peito", "Deitado no banco, empurra a barra", 40, 4, 10, "Barra"));
        servicoDeExercicios.cadastrar(new ExercicioDeForca(0, "Agachamento livre", "Pernas", "Agacha com a barra nas costas", 60, 4, 10, "Barra"));
        servicoDeExercicios.cadastrar(new ExercicioDeForca(0, "Leg press", "Pernas", "Empurra a plataforma com as pernas", 120, 4, 12, "Maquina"));
        servicoDeExercicios.cadastrar(new ExercicioDeForca(0, "Remada curvada", "Costas", "Tronco inclinado, puxa a barra", 35, 4, 10, "Barra"));
        servicoDeExercicios.cadastrar(new ExercicioDeForca(0, "Desenvolvimento", "Ombros", "Sentado, empurra os halteres pra cima", 20, 3, 12, "Halteres"));
        servicoDeExercicios.cadastrar(new ExercicioDeForca(0, "Rosca direta", "Biceps", "Flexiona o cotovelo com a barra", 15, 3, 12, "Barra W"));
        servicoDeExercicios.cadastrar(new ExercicioDeForca(0, "Levantamento terra", "Costas", "Tira a barra do chão com as costas retas", 80, 3, 8, "Barra"));

        // cardio (ids 8 a 14)
        servicoDeExercicios.cadastrar(new ExercicioCardio(0, "Esteira", "Pernas", "Corrida leve", 30, Intensidade.MEDIA));
        servicoDeExercicios.cadastrar(new ExercicioCardio(0, "Bicicleta ergometrica", "Pernas", "Pedalar com carga baixa", 40, Intensidade.BAIXA));
        servicoDeExercicios.cadastrar(new ExercicioCardio(0, "Eliptico", "Corpo inteiro", "Movimento continuo no eliptico", 25, Intensidade.MEDIA));
        servicoDeExercicios.cadastrar(new ExercicioCardio(0, "Corda", "Corpo inteiro", "Pular corda", 15, Intensidade.ALTA));
        servicoDeExercicios.cadastrar(new ExercicioCardio(0, "Remo", "Costas", "Remo ergometrico", 20, Intensidade.ALTA));
        servicoDeExercicios.cadastrar(new ExercicioCardio(0, "Escada", "Pernas", "Simulador de escada", 15, Intensidade.ALTA));
        servicoDeExercicios.cadastrar(new ExercicioCardio(0, "Caminhada inclinada", "Pernas", "Esteira com inclinação", 35, Intensidade.BAIXA));

        // funcionais (ids 15 a 21)
        servicoDeExercicios.cadastrar(new ExercicioFuncional(0, "Burpee", "Corpo inteiro", "Agacha, prancha, flexão e salto", 5, 40));
        servicoDeExercicios.cadastrar(new ExercicioFuncional(0, "Kettlebell swing", "Posterior", "Balança o kettlebell até a altura do peito", 4, 45));
        servicoDeExercicios.cadastrar(new ExercicioFuncional(0, "Box jump", "Pernas", "Salto em cima da caixa", 4, 30));
        servicoDeExercicios.cadastrar(new ExercicioFuncional(0, "Mountain climber", "Abdomen", "Na prancha, puxa os joelhos alternando", 5, 30));
        servicoDeExercicios.cadastrar(new ExercicioFuncional(0, "Afundo com salto", "Pernas", "Afundo trocando a perna no ar", 4, 40));
        servicoDeExercicios.cadastrar(new ExercicioFuncional(0, "Prancha dinamica", "Abdomen", "Sobe e desce da prancha alta pra baixa", 4, 45));
        servicoDeExercicios.cadastrar(new ExercicioFuncional(0, "Circuito de agachamento com salto", "Pernas", "Agachamento e salto sem parar", 5, 60));

        // treinos, cada um com seus exercicios (pelos ids de cima)
        criarTreinoInicial("Peito e ombro", Objetivo.HIPERTROFIA, "Segunda", 1, 5);
        criarTreinoInicial("Pernas pesado", Objetivo.HIPERTROFIA, "Terça", 2, 3);
        criarTreinoInicial("Costas e biceps", Objetivo.HIPERTROFIA, "Quarta", 4, 6, 7);
        criarTreinoInicial("Queima total", Objetivo.EMAGRECIMENTO, "Quinta", 8, 11, 15);
        criarTreinoInicial("Cardio leve", Objetivo.EMAGRECIMENTO, "Sexta", 9, 14);
        criarTreinoInicial("Circuito funcional", Objetivo.CONDICIONAMENTO, "Sabado", 16, 17, 18, 19);
        criarTreinoInicial("Resistencia", Objetivo.CONDICIONAMENTO, "Domingo", 12, 13, 20, 21);

        // alunos (ids 1 a 7) e os treinos de cada um
        criarAlunoInicial("Ana Souza", 28, 62.5, 1.65, LocalDate.of(2026, 2, 10), 4, 5);
        criarAlunoInicial("Bruno Lima", 35, 84.0, 1.80, LocalDate.of(2026, 3, 5), 1, 2, 3);
        criarAlunoInicial("Carla Mendes", 22, 58.0, 1.60, LocalDate.of(2026, 4, 20), 6);
        criarAlunoInicial("Diego Rocha", 41, 90.3, 1.75, LocalDate.of(2026, 5, 2), 2, 7);
        criarAlunoInicial("Elisa Martins", 30, 70.2, 1.68, LocalDate.of(2026, 6, 15), 4, 6);
        criarAlunoInicial("Felipe Andrade", 19, 68.0, 1.77, LocalDate.of(2026, 8, 1), 1, 3);
        criarAlunoInicial("Gabriela Nunes", 26, 55.4, 1.58, LocalDate.of(2026, 9, 12), 5, 7);

        // registros contando pra tras a partir de hoje, pra cair no mesmo mes e a frequencia ter o que mostrar.
        // Felipe e Gabriela ficam sem registro de proposito, pra aparecer no alerta de inatividade
        LocalDate hoje = LocalDate.now();
        criarRegistroInicial(1, 4, hoje.minusDays(1));
        criarRegistroInicial(1, 5, hoje.minusDays(3));
        criarRegistroInicial(2, 1, hoje.minusDays(2));
        criarRegistroInicial(2, 2, hoje.minusDays(5));
        criarRegistroInicial(3, 6, hoje);
        criarRegistroInicial(4, 7, hoje.minusDays(12));
        criarRegistroInicial(5, 4, hoje.minusDays(9));
    }

    // os "int..." deixam passar quantos ids de exercicio quiser
    private static void criarTreinoInicial(String nome, Objetivo objetivo, String dia, int... idsExercicios) {
        Treino treino = new Treino(0, nome, objetivo, dia);
        for (int id : idsExercicios) {
            servicoDeTreinos.adicionarExercicio(treino, id);
        }
        servicoDeTreinos.cadastrar(treino);
    }

    private static void criarAlunoInicial(String nome, int idade, double peso, double altura, LocalDate cadastro, int... idsTreinos) {
        Aluno aluno = new Aluno(0, cadastro, altura, peso, idade, nome);
        for (int id : idsTreinos) {
            aluno.getTreinos().add(servicoDeTreinos.buscarPorId(id));
        }
        servicoDeAlunos.cadastrar(aluno);
    }

    private static void criarRegistroInicial(int idAluno, int idTreino, LocalDate data) {
        servicoDeRegistros.registrarTreino(servicoDeAlunos.buscarPorId(idAluno), servicoDeTreinos.buscarPorId(idTreino), data);
    }
}
