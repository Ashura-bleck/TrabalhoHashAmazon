
public class funchash_teste {

    public static void main(String[] args) {

        char[] chave = {'A', 'n', 'a'};

        int tamanho = 3;

        int resultadofnv = funchash.fnv1a(chave, tamanho);

        int resultadopoli31 = funchash.poli31(chave, tamanho);

        int resultadodjb2 = funchash.djb2(chave, tamanho);

        System.out.println("fnv-1a: " + resultadofnv);

        System.out.println("polinomial 31: " + resultadopoli31);

        System.out.println("djb2: " + resultadodjb2);
    }
}
