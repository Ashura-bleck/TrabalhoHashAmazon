import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class leitorcsv {

    // total de colunas esperado no arquivo
    private static final int quantidadecolunas_esperada = 10;

    // posicoes das colunas usadas
    private static final int coluna_titulo = 1;
    private static final int coluna_usuario = 3;
    private static final int coluna_perfil = 4;
    private static final int coluna_nota = 6;

    // arquivo de entrada
    private BufferedReader arquivo;

    // guarda um caractere lido antes da hora
    private int caracterependente = -2;

    private int quantidadecolunas;

    // vetores dos campos usados
    private char[] titulo = new char[64];
    private int capacidadetitulo = 64;
    private int tamanhotitulo;

    private char[] usuario = new char[64];
    private int capacidadeusuario = 64;
    private int tamanhousuario;

    private char[] perfil = new char[64];
    private int capacidadeperfil = 64;
    private int tamanhoperfil;

    private char[] notatexto = new char[32];
    private int capacidadenotatexto = 32;
    private int tamanhonotatexto;

    // abre o arquivo em utf-8
    public void abrir(String caminho) throws IOException {
        arquivo = new BufferedReader(
            new FileReader(caminho, StandardCharsets.UTF_8)
        );
    }

    // fecha o arquivo
    public void fechar() throws IOException {
        if (arquivo != null) {
            arquivo.close();
        }
    }

    // le um registro completo (1 = ok, 0 = fim, -1 = erro)
    public int lerRegistro() throws IOException {

        zerarTamanhos();
        quantidadecolunas = 0;

        int colunaatual = 0;
        int dentroaspas = 0;
        int aspasfechadas = 0;
        int campotemconteudo = 0;
        int leualgo = 0;

        while (true) {

            int caractere = lerCaractere();

            // fim do arquivo
            if (caractere == -1) {

                if (leualgo == 0) {
                    return 0;
                }

                // nao pode acabar com aspas abertas
                if (dentroaspas == 1) {
                    return -1;
                }

                return finalizarRegistro(colunaatual);
            }

            // dentro de aspas
            if (dentroaspas == 1) {

                if (caractere == '"') {

                    int proximo = lerCaractere();

                    // duas aspas viram uma aspa no texto
                    if (proximo == '"') {

                        guardarCaractere('"', colunaatual);

                    } else {

                        dentroaspas = 0;
                        aspasfechadas = 1;

                        // guarda o proximo caractere para ler depois
                        caracterependente = proximo;
                    }

                } else {

                    // virgula e quebra de linha dentro de aspas fazem parte do campo
                    guardarCaractere((char) caractere, colunaatual);
                }

                leualgo = 1;

            }

            // depois de fechar aspas
            else if (aspasfechadas == 1) {

                if (caractere == ',') {

                    colunaatual++;
                    aspasfechadas = 0;
                    campotemconteudo = 0;
                    leualgo = 1;

                } else if (caractere == '\n' || caractere == '\r') {

                    if (caractere == '\r') {
                        consumirQuebraLF();
                    }

                    return finalizarRegistro(colunaatual);

                } else {

                    // apos fechar aspas so pode vir virgula ou fim de linha
                    return -1;
                }

            }

            // fora de aspas
            else {

                if (caractere == '"') {

                    // aspas so podem abrir no comeco do campo
                    if (campotemconteudo == 0) {

                        dentroaspas = 1;
                        campotemconteudo = 1;
                        leualgo = 1;

                    } else {

                        return -1;
                    }

                } else if (caractere == ',') {

                    colunaatual++;
                    campotemconteudo = 0;
                    leualgo = 1;

                } else if (caractere == '\n' || caractere == '\r') {

                    // pula linhas em branco entre registros
                    if (leualgo == 0 &&
                        colunaatual == 0 &&
                        campotemconteudo == 0) {

                        if (caractere == '\r') {
                            consumirQuebraLF();
                        }

                        continue;
                    }

                    if (caractere == '\r') {
                        consumirQuebraLF();
                    }

                    return finalizarRegistro(colunaatual);

                } else {

                    guardarCaractere((char) caractere, colunaatual);

                    campotemconteudo = 1;
                    leualgo = 1;
                }
            }
        }
    }

    // pega o caractere pendente ou le o proximo do arquivo
    private int lerCaractere() throws IOException {

        if (caracterependente != -2) {

            int valor = caracterependente;

            caracterependente = -2;

            return valor;
        }

        return arquivo.read();
    }

    // trata quebra de linha \r\n
    private void consumirQuebraLF() throws IOException {

        int proximo = lerCaractere();

        if (proximo != '\n') {
            caracterependente = proximo;
        }
    }

    // confere se o registro tem 10 colunas
    private int finalizarRegistro(int colunaatual) {

        quantidadecolunas = colunaatual + 1;

        if (quantidadecolunas == quantidadecolunas_esperada) {
            return 1;
        }

        return -1;
    }

    // reseta os tamanhos para o proximo registro
    private void zerarTamanhos() {

        tamanhotitulo = 0;
        tamanhousuario = 0;
        tamanhoperfil = 0;
        tamanhonotatexto = 0;
    }

    // dobra o tamanho do vetor quando enche
    private char[] aumentarVetor(
        char[] vetor,
        int capacidadeatual
    ) {

        char[] novovetor = new char[capacidadeatual * 2];

        for (int i = 0; i < capacidadeatual; i++) {
            novovetor[i] = vetor[i];
        }

        return novovetor;
    }

    // salva o caractere se for de uma das 4 colunas usadas
    private void guardarCaractere(
        char caractere,
        int colunaatual
    ) {

        if (colunaatual == coluna_titulo) {

            if (tamanhotitulo == capacidadetitulo) {
                titulo = aumentarVetor(titulo, capacidadetitulo);
                capacidadetitulo = capacidadetitulo * 2;
            }

            titulo[tamanhotitulo] = caractere;
            tamanhotitulo++;

        } else if (colunaatual == coluna_usuario) {

            if (tamanhousuario == capacidadeusuario) {
                usuario = aumentarVetor(usuario, capacidadeusuario);
                capacidadeusuario = capacidadeusuario * 2;
            }

            usuario[tamanhousuario] = caractere;
            tamanhousuario++;

        } else if (colunaatual == coluna_perfil) {

            if (tamanhoperfil == capacidadeperfil) {
                perfil = aumentarVetor(perfil, capacidadeperfil);
                capacidadeperfil = capacidadeperfil * 2;
            }

            perfil[tamanhoperfil] = caractere;
            tamanhoperfil++;

        } else if (colunaatual == coluna_nota) {

            if (tamanhonotatexto == capacidadenotatexto) {
                notatexto = aumentarVetor(
                    notatexto,
                    capacidadenotatexto
                );

                capacidadenotatexto = capacidadenotatexto * 2;
            }

            notatexto[tamanhonotatexto] = caractere;
            tamanhonotatexto++;
        }
    }

    // metodos para pegar os campos lidos

    public char[] obterTitulo() {
        return titulo;
    }

    public int obterTamanhoTitulo() {
        return tamanhotitulo;
    }

    public char[] obterUsuario() {
        return usuario;
    }

    public int obterTamanhoUsuario() {
        return tamanhousuario;
    }

    public char[] obterPerfil() {
        return perfil;
    }

    public int obterTamanhoPerfil() {
        return tamanhoperfil;
    }

    public char[] obterNotaTexto() {
        return notatexto;
    }

    public int obterTamanhoNotaTexto() {
        return tamanhonotatexto;
    }

    public int obterQuantidadeColunas() {
        return quantidadecolunas;
    }
}