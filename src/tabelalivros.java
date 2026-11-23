
public class tabelalivros {

    public static final int capacidade = 1000003;

    private nolivro[] tabela;

    private int tipofuncao;

    private int totallivros;

    private int totalcolisoes;

    public tabelalivros(int tipofuncaorecebida) {

        tabela = new nolivro[capacidade];

        if (tipofuncaorecebida >= 1 && tipofuncaorecebida <= 3) {
            tipofuncao = tipofuncaorecebida;
        } else {
            tipofuncao = 1;
        }

        totallivros = 0;
        totalcolisoes = 0;
    }

    private int calcularhash(char[] chave, int tamanho) {

        if (tipofuncao == 1) {
            return funchash.fnv1a(chave, tamanho);
        } else if (tipofuncao == 2) {
            return funchash.poli31(chave, tamanho);
        } else {
            return funchash.djb2(chave, tamanho);
        }
    }

    private int calcularposicao(char[] chave, int tamanho) {

        int h = calcularhash(chave, tamanho);

        int posicao = (h & 0x7FFFFFFF) % capacidade;

        return posicao;
    }

    private int comparartitulo(
        char[] titulo1,
        int tamanho1,
        char[] titulo2,
        int tamanho2
    ) {

        if (tamanho1 != tamanho2) {
            return 0;
        }

        for (int i = 0; i < tamanho1; i++) {

            if (titulo1[i] != titulo2[i]) {
                return 0;
            }
        }

        return 1;
    }

    public nolivro buscar(char[] titulo, int tamanho) {

        if (titulo == null || tamanho <= 0) {
            return null;
        }

        int posicao = calcularposicao(titulo, tamanho);

        nolivro atual = tabela[posicao];

        while (atual != null) {

            if (
                comparartitulo(
                    atual.titulo,
                    atual.tamanho,
                    titulo,
                    tamanho
                ) == 1
            ) {
                return atual;
            }

            atual = atual.proximo;
        }

        return null;
    }

    public nolivro inserirouatualizar(
        char[] titulo,
        int tamanho,
        double nota
    ) {

        if (
            titulo == null ||
            tamanho <= 0 ||
            nota < 1.0 ||
            nota > 5.0
        ) {
            return null;
        }

        int posicao = calcularposicao(titulo, tamanho);

        nolivro atual = tabela[posicao];

        while (atual != null) {

            if (
                comparartitulo(
                    atual.titulo,
                    atual.tamanho,
                    titulo,
                    tamanho
                ) == 1
            ) {

                atual.adicionarnota(nota);

                return atual;
            }

            atual = atual.proximo;
        }

        if (tabela[posicao] != null) {
            totalcolisoes++;
        }

        nolivro novo = new nolivro(titulo, tamanho);

        novo.adicionarnota(nota);

        novo.proximo = tabela[posicao];

        tabela[posicao] = novo;

        totallivros++;

        return novo;
    }

    public int obterquantidade() {
        return totallivros;
    }

    public int obtertotalcolisoes() {
        return totalcolisoes;
    }

    public nolivro[] obtertabela() {
        return tabela;
    }
}
