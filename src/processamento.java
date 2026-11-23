import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class processamento {

    private tabelalivros livros;
    private tabelausuarios usuarios;

    private int registroslidos;
    private int registrosvalidos;
    private int registrosdescartados;

    private int semusuario;
    private int semtitulo;
    private int notainvalida;

    private int errocsv;
    private int errocabecalho;

    private static final char[] cabecalho_titulo = {
        'T', 'i', 't', 'l', 'e'
    };

    private static final char[] cabecalho_usuario = {
        'U', 's', 'e', 'r', '_', 'i', 'd'
    };

    private static final char[] cabecalho_perfil = {
        'p', 'r', 'o', 'f', 'i', 'l', 'e', 'N', 'a', 'm', 'e'
    };

    private static final char[] cabecalho_nota = {
        'r', 'e', 'v', 'i', 'e', 'w', '/', 's', 'c', 'o', 'r', 'e'
    };

    public processamento(int funcoeslivros, int funcoesusuarios) {

        livros = new tabelalivros(funcoeslivros);

        usuarios = new tabelausuarios(funcoesusuarios);

        registroslidos = 0;
        registrosvalidos = 0;
        registrosdescartados = 0;

        semusuario = 0;
        semtitulo = 0;
        notainvalida = 0;

        errocsv = 0;
        errocabecalho = 0;
    }

    public void carregar(String caminho) throws IOException {

        leitorcsv leitor = new leitorcsv();

        try {

            leitor.abrir(caminho);

            int retorno = leitor.lerRegistro();

            if (
                retorno != 1 ||
                leitor.obterQuantidadeColunas() != 10
            ) {

                errocabecalho = 1;

                return;
            }

            if (verificarcabecalho(leitor) != 1) {

                errocabecalho = 1;

                return;
            }

            while (true) {

                retorno = leitor.lerRegistro();

                if (retorno == 0) {
                    break;
                }

                if (retorno == -1) {

                    errocsv = 1;

                    break;
                }

                registroslidos++;

                if (registroslidos % 500000 == 0) {

                    System.out.println(
                    "registros processados: " + registroslidos
                     );
                }

                char[] titulo = leitor.obterTitulo();
                int tamanhotitulo = leitor.obterTamanhoTitulo();

                char[] usuario = leitor.obterUsuario();
                int tamanhousuario = leitor.obterTamanhoUsuario();

                char[] perfil = leitor.obterPerfil();
                int tamanhoperfil = leitor.obterTamanhoPerfil();

                double nota = converterNota(
                    leitor.obterNotaTexto(),
                    leitor.obterTamanhoNotaTexto()
                );

                int faltausuario = estaVazio(
                    usuario,
                    tamanhousuario
                );

                int faltatitulo = estaVazio(
                    titulo,
                    tamanhotitulo
                );

                int faltanota = 0;

                if (nota < 1.0 || nota > 5.0) {
                    faltanota = 1;
                }

                if (faltausuario == 1) {
                    semusuario++;
                }

                if (faltatitulo == 1) {
                    semtitulo++;
                }

                if (faltanota == 1) {
                    notainvalida++;
                }

                if (
                    faltausuario == 1 ||
                    faltatitulo == 1 ||
                    faltanota == 1
                ) {

                    registrosdescartados++;

                    continue;
                }

                nolivro livro = livros.inserirouatualizar(
                    titulo,
                    tamanhotitulo,
                    nota
                );

                nousuario pessoa = usuarios.inserirouatualizar(
                    usuario,
                    tamanhousuario,
                    perfil,
                    tamanhoperfil
                );

                if (livro != null && pessoa != null) {

                    pessoa.registrarlivro(livro, nota);

                    registrosvalidos++;
                }
            }

        } finally {

            leitor.fechar();
        }
    }

    private int verificarcabecalho(leitorcsv leitor) {

        if (
            compararcampo(
                leitor.obterTitulo(),
                leitor.obterTamanhoTitulo(),
                cabecalho_titulo,
                5
            ) != 1
        ) {
            return 0;
        }

        if (
            compararcampo(
                leitor.obterUsuario(),
                leitor.obterTamanhoUsuario(),
                cabecalho_usuario,
                7
            ) != 1
        ) {
            return 0;
        }

        if (
            compararcampo(
                leitor.obterPerfil(),
                leitor.obterTamanhoPerfil(),
                cabecalho_perfil,
                11
            ) != 1
        ) {
            return 0;
        }

        if (
            compararcampo(
                leitor.obterNotaTexto(),
                leitor.obterTamanhoNotaTexto(),
                cabecalho_nota,
                12
            ) != 1
        ) {
            return 0;
        }

        return 1;
    }

    private int compararcampo(
        char[] recebido,
        int tamanhorecebido,
        char[] esperado,
        int tamanhoesperado
    ) {

        if (tamanhorecebido != tamanhoesperado) {
            return 0;
        }

        for (int i = 0; i < tamanhorecebido; i++) {

            if (recebido[i] != esperado[i]) {
                return 0;
            }
        }

        return 1;
    }

    private int estaVazio(char[] campo, int tamanho) {

        for (int i = 0; i < tamanho; i++) {

            if (
                campo[i] != ' ' &&
                campo[i] != '\t' &&
                campo[i] != '\r' &&
                campo[i] != '\n'
            ) {
                return 0;
            }
        }

        return 1;
    }

    private double converterNota(char[] texto, int tamanho) {

        int inicio = 0;
        int fim = tamanho - 1;

        while (
            inicio <= fim &&
            (texto[inicio] == ' ' || texto[inicio] == '\t')
        ) {
            inicio++;
        }

        while (
            fim >= inicio &&
            (texto[fim] == ' ' || texto[fim] == '\t')
        ) {
            fim--;
        }

        if (inicio > fim) {
            return -1.0;
        }

        int sinal = 1;

        if (texto[inicio] == '-') {

            sinal = -1;

            inicio++;

        } else if (texto[inicio] == '+') {

            inicio++;
        }

        double valor = 0.0;
        double fatordecimal = 0.1;

        int encontrouDigito = 0;
        int encontrouPonto = 0;

        for (int i = inicio; i <= fim; i++) {

            char caractere = texto[i];

            if (caractere >= '0' && caractere <= '9') {

                encontrouDigito = 1;

                int digito = caractere - '0';

                if (encontrouPonto == 0) {

                    valor = valor * 10.0 + digito;

                } else {

                    valor = valor + digito * fatordecimal;

                    fatordecimal = fatordecimal / 10.0;
                }

            } else if (
                caractere == '.' &&
                encontrouPonto == 0
            ) {

                encontrouPonto = 1;

            } else {

                return -1.0;
            }
        }

        if (encontrouDigito == 0) {
            return -1.0;
        }

        valor = valor * sinal;

        if (valor < 1.0 || valor > 5.0) {
            return -1.0;
        }

        return valor;
    }

    public void imprimirresumo() {

        System.out.println("===== resumo =====");

        if (errocabecalho == 1) {

            System.out.println("erro: cabecalho invalido");

            return;
        }

        System.out.println("registros lidos: " + registroslidos);

        System.out.println("registros validos: " + registrosvalidos);

        System.out.println("registros descartados: " + registrosdescartados);

        System.out.println("sem user_id: " + semusuario);

        System.out.println("sem title: " + semtitulo);

        System.out.println("nota invalida: " + notainvalida);

        System.out.println("livros distintos: " + livros.obterquantidade());

        System.out.println("usuarios distintos: " + usuarios.obterquantidade());

        if (errocsv == 1) {
            System.out.println("leitura interrompida por erro de csv");
        }
    }

    public tabelalivros obterlivros() {
        return livros;
    }

    public tabelausuarios obterusuarios() {
        return usuarios;
    }

    public int obterregistroslidos() {
        return registroslidos;
    }

    public int obterregistrosvalidos() {
        return registrosvalidos;
    }

    public int obterregistrosdescartados() {
        return registrosdescartados;
    }

    public int obtererrocsv() {
        return errocsv;
    }

    public int obtererrocabecalho() {
        return errocabecalho;
    }

    public void calcularmelhoreslivros() {

        nousuario[] vetusuarios = usuarios.obtertabela();

        for (int i = 0; i < tabelausuarios.capacidade; i++) {

            nousuario pessoa = vetusuarios[i];

            while (pessoa != null) {

                noavaliacaousuario atual = pessoa.avaliacoes;

                noavaliacaousuario melhor = null;

                while (atual != null) {

                    if (melhor == null) {

                        melhor = atual;

                    } else {

                        nolivro candidato = atual.livro;

                        nolivro escolhido = melhor.livro;

                        double media1 =
                            candidato.soma * escolhido.quantidade;

                        double media2 =
                            escolhido.soma * candidato.quantidade;

                        if (media1 > media2) {

                            melhor = atual;

                        } else if (media1 == media2) {

                            if (
                                comparartitulos(
                                    candidato.titulo,
                                    candidato.tamanho,
                                    escolhido.titulo,
                                    escolhido.tamanho
                                ) < 0
                            ) {

                                melhor = atual;
                            }
                        }
                    }

                    atual = atual.proximo;
                }

                pessoa.melhoravaliacao = melhor;

                pessoa = pessoa.proximo;
            }
        }
    }

    private int comparartitulos(
        char[] titulo1,
        int tamanho1,
        char[] titulo2,
        int tamanho2
    ) {

        int limite;

        if (tamanho1 < tamanho2) {
            limite = tamanho1;
        } else {
            limite = tamanho2;
        }

        for (int i = 0; i < limite; i++) {

            if (titulo1[i] < titulo2[i]) {
                return -1;
            }

            if (titulo1[i] > titulo2[i]) {
                return 1;
            }
        }

        if (tamanho1 < tamanho2) {
            return -1;
        }

        if (tamanho1 > tamanho2) {
            return 1;
        }

        return 0;
    }

    public void consultarusuarios(String caminho) throws IOException {

        int capacidadeusuario = 64;

        char[] usuario = new char[capacidadeusuario];

        int tamanhousuario = 0;

        int caracterependente = -2;

        int consultas = 0;

        try (
            BufferedReader arquivo = new BufferedReader(
                new FileReader(caminho, StandardCharsets.UTF_8)
            )
        ) {

            while (true) {

                int caractere;

                if (caracterependente != -2) {

                    caractere = caracterependente;

                    caracterependente = -2;

                } else {

                    caractere = arquivo.read();
                }

                if (caractere == -1) {

                    if (tamanhousuario > 0) {

                        imprimirconsulta(usuario, tamanhousuario);

                        consultas++;
                    }

                    break;
                }

                if (caractere == '\n' || caractere == '\r') {

                    if (tamanhousuario > 0) {

                        imprimirconsulta(usuario, tamanhousuario);

                        consultas++;

                        tamanhousuario = 0;
                    }

                    if (caractere == '\r') {

                        int proximo = arquivo.read();

                        if (proximo != '\n') {
                            caracterependente = proximo;
                        }
                    }

                    continue;
                }

                if (tamanhousuario == capacidadeusuario) {

                    char[] novousuario =
                        new char[capacidadeusuario * 2];

                    for (int i = 0; i < capacidadeusuario; i++) {
                        novousuario[i] = usuario[i];
                    }

                    usuario = novousuario;

                    capacidadeusuario = capacidadeusuario * 2;
                }

                usuario[tamanhousuario] = (char) caractere;

                tamanhousuario++;
            }
        }

        System.out.println("consultas realizadas: " + consultas);
    }

    private void imprimirconsulta(char[] usuario, int tamanho) {

        System.out.println();

        System.out.print("user_id: ");

        imprimircampo(usuario, tamanho);

        nousuario pessoa = usuarios.buscar(usuario, tamanho);

        if (pessoa == null) {

            System.out.println("usuario nao encontrado");

            return;
        }

        System.out.print("profileName: ");

        if (pessoa.tamanhoperfil > 0) {

            imprimircampo(pessoa.perfil, pessoa.tamanhoperfil);

        } else {

            System.out.println("(vazio)");
        }

        if (pessoa.melhoravaliacao != null) {

            System.out.print("melhor livro: ");

            imprimircampo(
                pessoa.melhoravaliacao.livro.titulo,
                pessoa.melhoravaliacao.livro.tamanho
            );

        } else {

            System.out.println("melhor livro nao encontrado");
        }
    }

    private void imprimircampo(char[] campo, int tamanho) {

        for (int i = 0; i < tamanho; i++) {
            System.out.print(campo[i]);
        }

        System.out.println();
    }

    public void executarexperimentos() {

        int quantidadeusuarios = usuarios.obterquantidade();

        char[][] chavesusuarios =
            new char[quantidadeusuarios][];

        int[] tamanhosusuarios =
            new int[quantidadeusuarios];

        int indiceusuario = 0;

        nousuario[] vetusuarios = usuarios.obtertabela();

        for (int i = 0; i < tabelausuarios.capacidade; i++) {

            nousuario atual = vetusuarios[i];

            while (atual != null) {

                chavesusuarios[indiceusuario] = atual.usuario;

                tamanhosusuarios[indiceusuario] = atual.tamanho;

                indiceusuario++;

                atual = atual.proximo;
            }
        }

        int quantidadetitulos = livros.obterquantidade();

        char[][] chavestitulos =
            new char[quantidadetitulos][];

        int[] tamanhostitulos =
            new int[quantidadetitulos];

        int indicetitulo = 0;

        nolivro[] vetlivros = livros.obtertabela();

        for (int i = 0; i < tabelalivros.capacidade; i++) {

            nolivro atual = vetlivros[i];

            while (atual != null) {

                chavestitulos[indicetitulo] = atual.titulo;

                tamanhostitulos[indicetitulo] = atual.tamanho;

                indicetitulo++;

                atual = atual.proximo;
            }
        }

        System.out.println();
        System.out.println("===== experimentos hash =====");

        experimentoshash.executartodos(
            chavesusuarios,
            tamanhosusuarios,
            quantidadeusuarios,
            chavestitulos,
            tamanhostitulos,
            quantidadetitulos
        );
    }
}