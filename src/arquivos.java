import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class arquivos {

    public static void gerarcsv(
        processamento programa,
        String caminho
    ) throws IOException {

        nousuario[] tabela =
            programa.obterusuarios().obtertabela();

        try (
            BufferedWriter saida = new BufferedWriter(
                new FileWriter(caminho, StandardCharsets.UTF_8)
            )
        ) {

            saida.write(
                "User_id,profileName,Title,media_livro,N_livro,nota_usuario"
            );

            saida.newLine();

            for (int i = 0; i < tabelausuarios.capacidade; i++) {

                nousuario pessoa = tabela[i];

                while (pessoa != null) {

                    if (pessoa.melhoravaliacao != null) {

                        nolivro livro =
                            pessoa.melhoravaliacao.livro;

                        saida.write(
                            campoCSV(pessoa.usuario, pessoa.tamanho)
                        );

                        saida.write(",");

                        saida.write(
                            campoCSV(pessoa.perfil, pessoa.tamanhoperfil)
                        );

                        saida.write(",");

                        saida.write(
                            campoCSV(livro.titulo, livro.tamanho)
                        );

                        saida.write(",");

                        saida.write(formatarmedia(livro.obtermedia()));

                        saida.write(",");

                        saida.write("" + livro.quantidade);

                        saida.write(",");

                        saida.write(
                            "" + pessoa.melhoravaliacao.notausuario
                        );

                        saida.newLine();
                    }

                    pessoa = pessoa.proximo;
                }
            }
        }
    }

    private static String campoCSV(char[] campo, int tamanho) {

        String resultado = "";

        int precisaaspas = 0;

        for (int i = 0; i < tamanho; i++) {

            if (
                campo[i] == ',' ||
                campo[i] == '"' ||
                campo[i] == '\n' ||
                campo[i] == '\r'
            ) {

                precisaaspas = 1;

                break;
            }
        }

        if (precisaaspas == 1) {
            resultado += '"';
        }

        for (int i = 0; i < tamanho; i++) {

            if (campo[i] == '"') {
                resultado += '"';
            }

            resultado += campo[i];
        }

        if (precisaaspas == 1) {
            resultado += '"';
        }

        return resultado;
    }

    private static String formatarmedia(double valor) {

        int arredondado = (int)(valor * 10000.0 + 0.5);

        int inteira = arredondado / 10000;

        int decimal = arredondado % 10000;

        String resultado = inteira + ".";

        if (decimal < 1000) {
            resultado += "0";
        }

        if (decimal < 100) {
            resultado += "0";
        }

        if (decimal < 10) {
            resultado += "0";
        }

        resultado += decimal;

        return resultado;
    }

    public static void gerarranking(
        tabelalivros livros,
        String caminho
    ) throws IOException {

        nolivro[] tabela = livros.obtertabela();

        nolivro[] melhores = new nolivro[10];

        int quantidade = 0;

        for (int i = 0; i < tabelalivros.capacidade; i++) {

            nolivro atual = tabela[i];

            while (atual != null) {

                if (atual.quantidade >= 100) {

                    int posicao = 0;

                    while (
                        posicao < quantidade &&
                        compararlivros(atual, melhores[posicao]) >= 0
                    ) {
                        posicao++;
                    }

                    if (posicao < 10) {

                        int ultimo = quantidade;

                        if (ultimo == 10) {
                            ultimo = 9;
                        }

                        for (int j = ultimo; j > posicao; j--) {
                            melhores[j] = melhores[j - 1];
                        }

                        melhores[posicao] = atual;

                        if (quantidade < 10) {
                            quantidade++;
                        }
                    }
                }

                atual = atual.proximo;
            }
        }

        try (
            BufferedWriter saida = new BufferedWriter(
                new FileWriter(caminho, StandardCharsets.UTF_8)
            )
        ) {

            saida.write("Title,media_livro,N_livro");

            saida.newLine();

            for (int i = 0; i < quantidade; i++) {

                nolivro livro = melhores[i];

                saida.write(
                    campoCSV(livro.titulo, livro.tamanho)
                );

                saida.write(",");

                saida.write(formatarmedia(livro.obtermedia()));

                saida.write(",");

                saida.write("" + livro.quantidade);

                saida.newLine();
            }
        }
    }

    private static int compararlivros(
        nolivro livro1,
        nolivro livro2
    ) {

        double media1 = livro1.soma * livro2.quantidade;

        double media2 = livro2.soma * livro1.quantidade;

        if (media1 > media2) {
            return -1;
        }

        if (media1 < media2) {
            return 1;
        }

        if (livro1.quantidade > livro2.quantidade) {
            return -1;
        }

        if (livro1.quantidade < livro2.quantidade) {
            return 1;
        }

        return comparartitulosranking(livro1, livro2);
    }

    private static int comparartitulosranking(
        nolivro livro1,
        nolivro livro2
    ) {

        int limite = livro1.tamanho;

        if (livro2.tamanho < limite) {
            limite = livro2.tamanho;
        }

        for (int i = 0; i < limite; i++) {

            if (livro1.titulo[i] < livro2.titulo[i]) {
                return -1;
            }

            if (livro1.titulo[i] > livro2.titulo[i]) {
                return 1;
            }
        }

        if (livro1.tamanho < livro2.tamanho) {
            return -1;
        }

        if (livro1.tamanho > livro2.tamanho) {
            return 1;
        }

        return 0;
    }

    public static void gerarordenado(
        nousuario[] usuarios,
        int quantidade,
        String caminho
    ) throws IOException {

        try (
            BufferedWriter saida = new BufferedWriter(
                new FileWriter(caminho, StandardCharsets.UTF_8)
            )
        ) {

            saida.write(
                "User_id,profileName,Title,media_livro,N_livro,nota_usuario"
            );

            saida.newLine();

            for (int i = 0; i < quantidade; i++) {

                nousuario pessoa = usuarios[i];

                if (pessoa.melhoravaliacao == null) {
                    continue;
                }

                nolivro livro = pessoa.melhoravaliacao.livro;

                saida.write(
                    campoCSV(pessoa.usuario, pessoa.tamanho)
                );

                saida.write(",");

                saida.write(
                    campoCSV(pessoa.perfil, pessoa.tamanhoperfil)
                );

                saida.write(",");

                saida.write(
                    campoCSV(livro.titulo, livro.tamanho)
                );

                saida.write(",");

                saida.write(formatarmedia(livro.obtermedia()));

                saida.write(",");

                saida.write("" + livro.quantidade);

                saida.write(",");

                saida.write(
                    "" + pessoa.melhoravaliacao.notausuario
                );

                saida.newLine();
            }
        }
    }
}