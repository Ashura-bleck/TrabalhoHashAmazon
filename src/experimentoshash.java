
public class experimentoshash {

    public static char[] criarchavefalha(
        char[] chave,
        int tamanho
    ) {

        char[] novachave = new char[tamanho + 1];

        // copia os caracteres originais
        for (int i = 0; i < tamanho; i++) {
            novachave[i] = chave[i];
        }

        // coloca caractere extra no final
        novachave[tamanho] = (char) 1;

        return novachave;
    }

    public static void executar(
        char[][] chaves,
        int[] tamanhos,
        int quantidade,
        int tipofuncao,
        String nomefuncao
    ) {

        double somatempoinsercao = 0.0;
        double somatempobusca = 0.0;

        tabelahash tabela = null;

        // repete 3 vezes para calcular a media dos tempos
        for (int repeticao = 0; repeticao < 3; repeticao++) {

            tabela = new tabelahash(tipofuncao);

            // mede tempo de insercao
            double inicioinsercao = System.nanoTime();

            for (int i = 0; i < quantidade; i++) {
                tabela.inserir(chaves[i], tamanhos[i]);
            }

            double fiminsercao = System.nanoTime();

            double tempoinsercao =
                (fiminsercao - inicioinsercao) / 1000000.0;

            somatempoinsercao += tempoinsercao;

            // mede tempo das buscas com sucesso
            double iniciobusca = System.nanoTime();

            for (int i = 0; i < quantidade; i++) {
                tabela.buscar(chaves[i], tamanhos[i]);
            }

            double fimbusca = System.nanoTime();

            double tempobusca =
                (fimbusca - iniciobusca) / 1000000.0;

            somatempobusca += tempobusca;

            // faz buscas sem sucesso fora da medicao de tempo
            for (int i = 0; i < quantidade; i++) {

                char[] chavefalha = criarchavefalha(
                    chaves[i],
                    tamanhos[i]
                );

                tabela.buscar(chavefalha, tamanhos[i] + 1);
            }
        }

        // calcula medias dos tempos
        double mediatempoinsercao = somatempoinsercao / 3.0;
        double mediatempobusca = somatempobusca / 3.0;

        // calcula medias de sondagens da ultima repeticao
        double mediasucesso =
            (double) tabela.obterTotalSondagensBuscaSucesso()
            / quantidade;

        double mediafalha =
            (double) tabela.obterTotalSondagensBuscaFalha()
            / quantidade;

        // apresenta os resultados
        System.out.println("--------------------------------");
        System.out.println("funcao: " + nomefuncao);
        System.out.println("colisoes: " + tabela.obterTotalColisoes());
        System.out.println("sondagens sucesso: " + mediasucesso);
        System.out.println("sondagens falha: " + mediafalha);
        System.out.println("pior caso: " + tabela.obterPiorCaso());
        System.out.println("tempo medio insercao (ms): " + mediatempoinsercao);
        System.out.println("tempo medio busca (ms): " + mediatempobusca);
    }

    public static void executartodos(
        char[][] chavesusuarios,
        int[] tamanhosusuarios,
        int quantidadeusuarios,
        char[][] chavestitulos,
        int[] tamanhostitulos,
        int quantidadetitulos
    ) {

        // testes com usuarios
        executar(
            chavesusuarios,
            tamanhosusuarios,
            quantidadeusuarios,
            1,
            "FNV-1a"
        );

        executar(
            chavesusuarios,
            tamanhosusuarios,
            quantidadeusuarios,
            2,
            "Polinomial 31"
        );

        executar(
            chavesusuarios,
            tamanhosusuarios,
            quantidadeusuarios,
            3,
            "djb2"
        );

        // testes com titulos
        executar(
            chavestitulos,
            tamanhostitulos,
            quantidadetitulos,
            1,
            "FNV-1a"
        );

        executar(
            chavestitulos,
            tamanhostitulos,
            quantidadetitulos,
            2,
            "Polinomial 31"
        );

        executar(
            chavestitulos,
            tamanhostitulos,
            quantidadetitulos,
            3,
            "djb2"
        );
    }
}
