public class funchash_teste {

    public static  void main(String[] args) {

        char[] chave = new char[3];

        chave[0] = 'A';
        chave[0] = 'n';
        chave[0] = 'a';

        int tamanho = 3;  

        int resultadofnv = funchash.Fnv1a (chave, tamanho);
        
        int resultadopoli31 = funchash.poli31 (chave, tamanho);

        int resultadodjb2 = funchash.djb2 (chave, tamanho);

        System.out.println("FNV-1a; " + resultadofnv);

        System.out.println("Polinomial31; " + resultadopoli31);

        System.out.println("djb2; " + resultadodjb2);

    
    }
    
}
