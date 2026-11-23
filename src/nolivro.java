
public class nolivro {

    char[] titulo;
    int tamanho;

    double soma;
    int quantidade;

    nolivro proximo;

    public nolivro(char[] titulorecebido, int tamanhorecebido) {

        titulo = new char[tamanhorecebido];

        tamanho = tamanhorecebido;

        for (int i = 0; i < tamanhorecebido; i++) {
            titulo[i] = titulorecebido[i];
        }

        soma = 0.0;
        quantidade = 0;

        proximo = null;
    }

    public void adicionarnota(double nota) {

        soma += nota;

        quantidade++;
    }

    public double obtermedia() {

        if (quantidade == 0) {
            return 0.0;
        }

        return soma / quantidade;
    }
}
