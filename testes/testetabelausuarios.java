
public class testetabelausuarios {

    public static void main(String[] args) {

        tabelausuarios tabela = new tabelausuarios(2);

        char[] usuario1 = {'U', '0', '0', '1'};
        char[] usuario2 = {'U', '0', '0', '2'};

        char[] perfilvazio = new char[0];

        char[] perfilmaria = {
            'M', 'a', 'r', 'i', 'a'
        };

        char[] perfiljoao = {
            'J', 'o', 'a', 'o'
        };

        char[] perfilana = {
            'A', 'n', 'a'
        };

        tabela.inserirouatualizar(
            usuario1, 4, perfilvazio, 0
        );

        tabela.inserirouatualizar(
            usuario1, 4, perfilmaria, 5
        );

        tabela.inserirouatualizar(
            usuario1, 4, perfilana, 3
        );

        tabela.inserirouatualizar(
            usuario2, 4, perfiljoao, 4
        );

        nousuario resultado1 = tabela.buscar(usuario1, 4);

        nousuario resultado2 = tabela.buscar(usuario2, 4);

        char[] inexistente = {'U', '0', '0', '3'};

        nousuario resultado3 = tabela.buscar(inexistente, 4);

        System.out.println("usuario u001:");

        imprimircampo(resultado1.perfil, resultado1.tamanhoperfil);

        System.out.println();

        System.out.println("usuario u002:");

        imprimircampo(resultado2.perfil, resultado2.tamanhoperfil);

        System.out.println();

        System.out.println("usuario u003 encontrado: " + (resultado3 != null));

        System.out.println();

        System.out.println(
            "total de usuarios: " + tabela.obterquantidade()
        );
    }

    public static void imprimircampo(char[] campo, int tamanho) {

        for (int i = 0; i < tamanho; i++) {
            System.out.print(campo[i]);
        }

        System.out.println();
    }
}
