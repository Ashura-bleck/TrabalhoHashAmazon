public class funchash {

    //FNV1a. Começa com um valor fixo e, para cada caractere, aplica um ou exclusivo (XOR,operador ˆ em Java) e depois multiplica por um número primo fixo:

    public static int Fnv1a(char[] chave, int tamanho){

        int h = 0x811C9DC5;

        for (int i = 0; i < tamanho; i++) {

            h = h ^ chave[i];

            h = h * 16777619;
        }

        return  h;

    }

    //Polinomial de base 31. Trata a chave como um número escrito na base 31. Pela regra de Horner:
    
    public static int poli31(char[] chave, int tamanho) {

        int h = 0; 

        for (int i = 0; i < tamanho; i++) {

            h = 31 * h + chave[i];

        }

        return h;
    }

    //djb2. Proposta por Daniel J. Bernstein, é parecida com a polinomial, mas usa a base 33 e começa em 5381:

    public static int djb2(char[] chave, int tamanho) {

        int h = 5381;

        for (int i = 0; i < tamanho; i++) {

            h = 33 * h + chave [i]; 
        }

        return h;
    }
}
