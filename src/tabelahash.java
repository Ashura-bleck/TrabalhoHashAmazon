public class tabelahash {

    private static final int capacidade = 1000003;

    private nohash[] tabela;

    private int tipofuncao;

    public tabelahash(int tipofuncaorecebida) {

        tabela = new nohash[capacidade];

        if (tipofuncaorecebida >= 1 && tipofuncaorecebida <= 3) {

            tipofuncao = tipofuncaorecebida;

        } else {

            tipofuncao = 1;
        }
    }

    // calcula hash com as funcoes
    private int calhash(char[] chave, int tamanho) {

        if (tipofuncao == 1) {

            return funchash.Fnv1a(chave, tamanho);

        } else if (tipofuncao == 2) {

            return funchash.poli31(chave, tamanho);

        } else {

            return funchash.djb2(chave, tamanho);
        }
    }

    // calcula a posicao do vetor
    private int calposicao(char[] chave, int tamanho) {

        int h = calhash(chave, tamanho);

        int posicao = (h & 0x7FFFFFFF) % capacidade;

        return posicao;
    }

    // compara as chaves caractere por caractere
    private int comparchave(
        char[] chave1,
        int tamanho1,
        char[] chave2,
        int tamanho2
    ) {

        if (tamanho1 != tamanho2) {

            return 0;
        }

        for (int i = 0; i < tamanho1; i++) {

            if (chave1[i] != chave2[i]) {

                return 0;
            }
        }

        return 1;
    }

    // insere na tabela, retorno:
    // 0 = inseriu sem colisao
    // 1 = inseriu com colisao
    // -1 = chave existente ou tamanho invalido
    public int inserir(char[] chave, int tamanho) {

        if (tamanho < 0) {

            return -1;
        }

        int posicao = calposicao(chave, tamanho);

        nohash atual = tabela[posicao];

        // verifica se a chave ja existe
        while (atual != null) {

            if (comparchave(
                atual.chave,
                atual.tamanho,
                chave,
                tamanho
            ) == 1) {

                return -1;
            }

            atual = atual.proximo;
        }

        int colisao = 0;

        if (tabela[posicao] != null) {

            colisao = 1;
        }

        // cria novo no e insere no inicio da lista
        nohash novo = new nohash(chave, tamanho);

        novo.proximo = tabela[posicao];

        tabela[posicao] = novo;

        return colisao;
    }

    // busca uma chave na tabela
    // retorno:
    // 1 = encontrou
    // 0 = nao encontrou
    public int buscar(char[] chave, int tamanho) {

        if (tamanho < 0) {

            return 0;
        }

        int posicao = calposicao(chave, tamanho);

        nohash atual = tabela[posicao];

        while (atual != null) {

            if (comparchave(
                atual.chave,
                atual.tamanho,
                chave,
                tamanho
            ) == 1) {

                return 1;
            }

            atual = atual.proximo;
        }

        return 0;
    }
}