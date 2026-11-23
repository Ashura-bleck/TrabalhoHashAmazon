
public class testetabelalivros {

    public static void main(String[] args) {

        tabelalivros tabela = new tabelalivros(2);

        char[] livroa = {
            'L', 'i', 'v', 'r', 'o', ' ', 'A'
        };

        char[] livrob = {
            'L', 'i', 'v', 'r', 'o', ' ', 'B'
        };

        tabela.inserirouatualizar(livroa, 7, 5.0);
        tabela.inserirouatualizar(livroa, 7, 3.0);

        tabela.inserirouatualizar(livrob, 7, 4.0);
        tabela.inserirouatualizar(livrob, 7, 5.0);

        nolivro resultadoa = tabela.buscar(livroa, 7);
        nolivro resultadob = tabela.buscar(livrob, 7);

        System.out.println("livro a:");

        System.out.println("soma: " + resultadoa.soma);
        System.out.println("quantidade: " + resultadoa.quantidade);
        System.out.println("media: " + resultadoa.obtermedia());

        System.out.println();

        System.out.println("livro b:");

        System.out.println("soma: " + resultadob.soma);
        System.out.println("quantidade: " + resultadob.quantidade);
        System.out.println("media: " + resultadob.obtermedia());

        System.out.println();

        System.out.println("total de livros: " + tabela.obterquantidade());
    }
}
