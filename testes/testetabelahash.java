
public class testetabelahash {

    public static void main(String[] args) {

        tabelahash tabela = new tabelahash(2);

        char[] chaveaa = {'A', 'a'};
        char[] chavebb = {'B', 'B'};
        char[] chavecc = {'C', 'C'};

        int resultadoaa = tabela.inserir(chaveaa, 2);
        int resultadobb = tabela.inserir(chavebb, 2);

        int buscaa = tabela.buscar(chaveaa, 2);
        int buscab = tabela.buscar(chavebb, 2);
        int buscac = tabela.buscar(chavecc, 2);

        System.out.println("insercao aa: " + resultadoaa);
        System.out.println("insercao bb: " + resultadobb);

        System.out.println("busca aa: " + buscaa);
        System.out.println("busca bb: " + buscab);
        System.out.println("busca cc: " + buscac);
    }
}
