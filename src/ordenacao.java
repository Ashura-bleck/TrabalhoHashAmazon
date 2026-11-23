
public class ordenacao {

    public static nousuario[] ordenar(tabelausuarios tabela) {

        int quantidade = tabela.obterquantidade();

        nousuario[] usuarios = new nousuario[quantidade];

        nousuario[] tabelaoriginal = tabela.obtertabela();

        int indice = 0;

        for (int i = 0; i < tabelausuarios.capacidade; i++) {

            nousuario atual = tabelaoriginal[i];

            while (atual != null) {

                usuarios[indice] = atual;

                indice++;

                atual = atual.proximo;
            }
        }

        nousuario[] auxiliar = new nousuario[quantidade];

        if (quantidade > 1) {

            mergesort(
                usuarios,
                auxiliar,
                0,
                quantidade - 1
            );
        }

        return usuarios;
    }

    private static void mergesort(
        nousuario[] vetor,
        nousuario[] auxiliar,
        int inicio,
        int fim
    ) {

        if (inicio >= fim) {
            return;
        }

        int meio = inicio + (fim - inicio) / 2;

        mergesort(vetor, auxiliar, inicio, meio);

        mergesort(vetor, auxiliar, meio + 1, fim);

        intercalar(vetor, auxiliar, inicio, meio, fim);
    }

    private static void intercalar(
        nousuario[] vetor,
        nousuario[] auxiliar,
        int inicio,
        int meio,
        int fim
    ) {

        int i = inicio;

        int j = meio + 1;

        int k = inicio;

        while (i <= meio && j <= fim) {

            if (comparar(vetor[i], vetor[j]) <= 0) {

                auxiliar[k] = vetor[i];

                i++;

            } else {

                auxiliar[k] = vetor[j];

                j++;
            }

            k++;
        }

        while (i <= meio) {

            auxiliar[k] = vetor[i];

            i++;

            k++;
        }

        while (j <= fim) {

            auxiliar[k] = vetor[j];

            j++;

            k++;
        }

        for (int posicao = inicio; posicao <= fim; posicao++) {

            vetor[posicao] = auxiliar[posicao];
        }
    }

    private static int comparar(nousuario usuario1, nousuario usuario2) {

        noavaliacaousuario avaliacao1 =
            usuario1.melhoravaliacao;

        noavaliacaousuario avaliacao2 =
            usuario2.melhoravaliacao;

        if (avaliacao1 == null && avaliacao2 != null) {
            return 1;
        }

        if (avaliacao1 != null && avaliacao2 == null) {
            return -1;
        }

        if (avaliacao1 != null && avaliacao2 != null) {

            nolivro livro1 = avaliacao1.livro;

            nolivro livro2 = avaliacao2.livro;

            double media1 =
                livro1.soma * livro2.quantidade;

            double media2 =
                livro2.soma * livro1.quantidade;

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
        }

        return compararusuarios(usuario1, usuario2);
    }

    private static int compararusuarios(
        nousuario usuario1,
        nousuario usuario2
    ) {

        int limite = usuario1.tamanho;

        if (usuario2.tamanho < limite) {
            limite = usuario2.tamanho;
        }

        for (int i = 0; i < limite; i++) {

            if (usuario1.usuario[i] < usuario2.usuario[i]) {
                return -1;
            }

            if (usuario1.usuario[i] > usuario2.usuario[i]) {
                return 1;
            }
        }

        if (usuario1.tamanho < usuario2.tamanho) {
            return -1;
        }

        if (usuario1.tamanho > usuario2.tamanho) {
            return 1;
        }

        return 0;
    }
}
