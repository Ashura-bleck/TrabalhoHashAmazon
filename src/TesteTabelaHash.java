public class TesteTabelaHash {

    public static void main(String[] args) {

        // cria tabela usando a funcao polinomial 31

        tabelahash tabela = new tabelahash(2);

        char[] chaveAa = {'A', 'a'};
        
        char[] chaveBB = {'B','B'};

        char[] chaveCC = {'C', 'C'};

        //insere Aa e BB faz colisao polinomial 31

        int resultadoAa = tabela.inserir(chaveAa, 2);

        int resultadoBB = tabela.inserir(chaveBB, 2);

        // realiza as buscas sem a CC nao fui inserido

        int buscaAa = tabela.buscar(chaveAa, 2);

        int buscaBB = tabela.buscar(chaveBB, 2);

        int buscaCC = tabela.buscar(chaveCC, 2);

        System.out.println("resultado da insercao Aa: " + resultadoAa);

        System.out.println("resultado da insercao BB: " + resultadoBB);

        System.out.println("resultado da inser Aa: " + buscaAa);

        System.out.println("resultado da inser CC: " + buscaBB);

        System.out.println("resultado da inser CC: " + buscaCC);


    }
    
}
