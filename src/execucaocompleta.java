
import java.io.IOException;

public class execucaocompleta {

    public static void main(String[] args) {

        processamento programa = new processamento(1, 1);

        try {

            System.out.println("iniciando processamento da base completa");

            double inicioleitura = System.nanoTime();

            programa.carregar("dados/Books_rating.csv");

            double fimleitura = System.nanoTime();

            double tempoleitura =
                (fimleitura - inicioleitura) / 1000000.0;

            programa.imprimirresumo();

            if (
                programa.obtererrocabecalho() == 1 ||
                programa.obtererrocsv() == 1
            ) {

                System.out.println("processamento interrompido");

                return;
            }

            System.out.println("calculando melhores livros");

            double iniciocalculo = System.nanoTime();

            programa.calcularmelhoreslivros();

            double fimcalculo = System.nanoTime();

            double tempocalculo =
                (fimcalculo - iniciocalculo) / 1000000.0;

            System.out.println("ordenando usuarios");

            double inicioordenacao = System.nanoTime();

            nousuario[] usuariosordenados =
                ordenacao.ordenar(programa.obterusuarios());

            double fimordenacao = System.nanoTime();

            double tempoordenacao =
                (fimordenacao - inicioordenacao) / 1000000.0;

            System.out.println("gerando arquivos csv");

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

            System.out.println("consultando usuarios");

            double inicioconsultas = System.nanoTime();

            programa.consultarusuarios("dados/user_id.txt");

            double fimconsultas = System.nanoTime();

            double tempoconsultas =
                (fimconsultas - inicioconsultas) / 1000000.0;

            System.out.println();
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

            System.out.println();
            System.out.println("===== experimentos hash =====");

            programa.executarexperimentos();

            System.out.println();
            System.out.println("processamento finalizado");

        } catch (IOException erro) {

            System.out.println("erro ao acessar os arquivos");

            System.out.println(erro.getMessage());
        }
    }
}

