
public class tabelausuarios {

    public static final int capacidade = 1000003;

    private nousuario[] tabela;

    private int tipofuncao;

    private int totalusuarios;

    private int totalcolisoes;

    public tabelausuarios(int tipofuncaorecebida) {

        tabela = new nousuario[capacidade];

        if (tipofuncaorecebida >= 1 && tipofuncaorecebida <= 3) {
            tipofuncao = tipofuncaorecebida;
        } else {
            tipofuncao = 1;
        }

        totalusuarios = 0;
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

    private int compararusuarios(
        char[] usuario1,
        int tamanho1,
        char[] usuario2,
        int tamanho2
    ) {

        if (tamanho1 != tamanho2) {
            return 0;
        }

        for (int i = 0; i < tamanho1; i++) {

            if (usuario1[i] != usuario2[i]) {
                return 0;
            }
        }

        return 1;
    }

    public nousuario buscar(char[] usuario, int tamanho) {

        if (usuario == null || tamanho <= 0) {
            return null;
        }

        int posicao = calcularposicao(usuario, tamanho);

        nousuario atual = tabela[posicao];

        while (atual != null) {

            if (
                compararusuarios(
                    atual.usuario,
                    atual.tamanho,
                    usuario,
                    tamanho
                ) == 1
            ) {
                return atual;
            }

            atual = atual.proximo;
        }

        return null;
    }

    public nousuario inserirouatualizar(
        char[] usuario,
        int tamanho,
        char[] perfil,
        int tamanhoperfil
    ) {

        if (usuario == null || tamanho <= 0) {
            return null;
        }

        int posicao = calcularposicao(usuario, tamanho);

        nousuario atual = tabela[posicao];

        while (atual != null) {

            if (
                compararusuarios(
                    atual.usuario,
                    atual.tamanho,
                    usuario,
                    tamanho
                ) == 1
            ) {

                atual.atualizarperfil(perfil, tamanhoperfil);

                return atual;
            }

            atual = atual.proximo;
        }

        if (tabela[posicao] != null) {
            totalcolisoes++;
        }

        nousuario novo = new nousuario(usuario, tamanho);

        novo.atualizarperfil(perfil, tamanhoperfil);

        novo.proximo = tabela[posicao];

        tabela[posicao] = novo;

        totalusuarios++;

        return novo;
    }

    public int obterquantidade() {
        return totalusuarios;
    }

    public int obtertotalcolisoes() {
        return totalcolisoes;
    }

    public nousuario[] obtertabela() {
        return tabela;
    }
}
