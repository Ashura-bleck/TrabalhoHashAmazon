
public class testeexperimentos {

    public static void main(String[] args) {

        // chaves de usuarios
        char[][] chavesusuarios = {
            {'A', 'n', 'a'},
            {'J', 'o', 'a', 'o'},
            {'M', 'a', 'r', 'i', 'a'},
            {'P', 'e', 'd', 'r', 'o'},
            {'C', 'a', 'r', 'l', 'o', 's'}
        };

        int[] tamanhosusuarios = {
            3, 4, 5, 5, 6
        };

        int quantidadeusuarios = 5;

        // chaves de titulos
        char[][] chavestitulos = {
            {'L', 'i', 'v', 'r', 'o', ' ', 'A'},
            {'L', 'i', 'v', 'r', 'o', ' ', 'B'},
            {'L', 'i', 'v', 'r', 'o', ' ', 'C'},
            {'L', 'i', 'v', 'r', 'o', ' ', 'D'},
            {'L', 'i', 'v', 'r', 'o', ' ', 'E'}
        };

        int[] tamanhostitulos = {
            7, 7, 7, 7, 7
        };

        int quantidadetitulos = 5;

        // roda os experimentos
        experimentoshash.executartodos(
            chavesusuarios,
            tamanhosusuarios,
            quantidadeusuarios,
            chavestitulos,
            tamanhostitulos,
            quantidadetitulos
        );
    }
}
