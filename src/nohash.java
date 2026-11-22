public class nohash {

    char[] chave;
    int tamanho;
    nohash proximo;

    public nohash(char[] chaverecebida, int tamanhorecebido) {
        
        chave = new char[tamanhorecebido];
        tamanho = tamanhorecebido;
        
        //copia de caracter da chave 
        for (int i = 0; i < tamanhorecebido; i++) {

            chave[i] = chaverecebida[i];
        }

        proximo = null;
    }
    
}
