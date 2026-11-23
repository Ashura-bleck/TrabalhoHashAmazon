
import java.io.IOException;

public class testeprocessamento {

    public static void main(String[] args) {

        processamento programa = new processamento(1, 1);

        try {

            double inicioleitura = System.nanoTime();

            programa.carregar("dados/amostra.csv");

            double fimleitura = System.nanoTime();

            double tempoleitura =
                (fimleitura - inicioleitura) / 1000000.0;

            if (programa.obtererrocabecalho() == 1) {

                programa.imprimirresumo();

                return;
            }

            double iniciocalculo = System.nanoTime();

            programa.calcularmelhoreslivros();
            
            programa.executarexperimentos();

            double fimcalculo = System.nanoTime();

            double tempocalculo =
                (fimcalculo - iniciocalculo) / 1000000.0;

            double inicioordenacao = System.nanoTime();

            nousuario[] usuariosordenados =
                ordenacao.ordenar(programa.obterusuarios());

            double fimordenacao = System.nanoTime();

            double tempoordenacao =
                (fimordenacao - inicioordenacao) / 1000000.0;

            double iniciogravacao = System.nanoTime();

            arquivos.gerarcsv(
                programa,
                "saidas/usuario_melhor_livro.csv"
            );

            arquivos.gerarranking(
                programa.obterlivros(),
                "saidas/ranking_livros.csv"
            );

            arquivos.gerarordenado(
                usuariosordenados,
                programa.obterusuarios().obterquantidade(),
                "saidas/usuario_melhor_livro_ordenado.csv"
            );

            double fimgravacao = System.nanoTime();

            double tempogravacao =
                (fimgravacao - iniciogravacao) / 1000000.0;

            double inicioconsultas = System.nanoTime();

            programa.consultarusuarios("dados/user_id.txt");

            double fimconsultas = System.nanoTime();

            double tempoconsultas =
                (fimconsultas - inicioconsultas) / 1000000.0;

            programa.imprimirresumo();

            System.out.println();
            System.out.println("===== melhores livros =====");

            nousuario[] tabela =
                programa.obterusuarios().obtertabela();

            for (int i = 0; i < tabelausuarios.capacidade; i++) {

                nousuario pessoa = tabela[i];

                while (pessoa != null) {

                    System.out.print("usuario: ");

                    imprimircampo(
                        pessoa.usuario,
                        pessoa.tamanho
                    );

                    if (pessoa.melhoravaliacao != null) {

                        nolivro livro =
                            pessoa.melhoravaliacao.livro;

                        System.out.print("melhor livro: ");

                        imprimircampo(
                            livro.titulo,
                            livro.tamanho
                        );

                        System.out.println(
                            "media global: " + livro.obtermedia()
                        );

                        System.out.println(
                            "nota do usuario: " +
                            pessoa.melhoravaliacao.notausuario
                        );
                    }

                    System.out.println();

                    pessoa = pessoa.proximo;
                }
            }

            System.out.println("===== tempos de execucao =====");

            System.out.println(
                "leitura e indexacao: " + tempoleitura + " ms"
            );

            System.out.println(
                "calculo do melhor livro: " + tempocalculo + " ms"
            );

            System.out.println(
                "ordenacao: " + tempoordenacao + " ms"
            );

            System.out.println(
                "gravacao dos arquivos: " + tempogravacao + " ms"
            );

            System.out.println(
                "consultas: " + tempoconsultas + " ms"
            );

        } catch (IOException erro) {

            System.out.println("erro ao acessar os arquivos");
        }
    }

    public static void imprimircampo(char[] campo, int tamanho) {

        for (int i = 0; i < tamanho; i++) {
            System.out.print(campo[i]);
        }

        System.out.println();
    }

}
