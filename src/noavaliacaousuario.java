
public class noavaliacaousuario {

    nolivro livro;

    double notausuario;

    noavaliacaousuario proximo;

    public noavaliacaousuario(nolivro livrorecebido, double notarecebida) {

        livro = livrorecebido;

        notausuario = notarecebida;

        proximo = null;
    }

    public void atualizarnota(double nota) {

        if (nota > notausuario) {
            notausuario = nota;
        }
    }
}
