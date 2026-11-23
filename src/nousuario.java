
public class nousuario {

    char[] usuario;
    int tamanho;

    char[] perfil;
    int tamanhoperfil;

    noavaliacaousuario avaliacoes;

    noavaliacaousuario melhoravaliacao;

    nousuario proximo;

    public nousuario(char[] usuariorecebido, int tamanhorecebido) {

        usuario = new char[tamanhorecebido];

        tamanho = tamanhorecebido;

        for (int i = 0; i < tamanhorecebido; i++) {
            usuario[i] = usuariorecebido[i];
        }

        perfil = new char[0];
        tamanhoperfil = 0;

        avaliacoes = null;
        melhoravaliacao = null;

        proximo = null;
    }

    public void atualizarperfil(char[] perfilrecebido, int tamanhorecebido) {

        if (tamanhoperfil > 0 || perfilrecebido == null) {
            return;
        }

        int vazio = 1;

        for (int i = 0; i < tamanhorecebido; i++) {

            char caractere = perfilrecebido[i];

            if (
                caractere != ' ' &&
                caractere != '\t' &&
                caractere != '\r' &&
                caractere != '\n'
            ) {
                vazio = 0;
                break;
            }
        }

        if (vazio == 1) {
            return;
        }

        perfil = new char[tamanhorecebido];

        for (int i = 0; i < tamanhorecebido; i++) {
            perfil[i] = perfilrecebido[i];
        }

        tamanhoperfil = tamanhorecebido;
    }

    public void registrarlivro(nolivro livrorecebido, double nota) {

        if (livrorecebido == null) {
            return;
        }

        noavaliacaousuario atual = avaliacoes;

        while (atual != null) {

            if (atual.livro == livrorecebido) {

                atual.atualizarnota(nota);

                return;
            }

            atual = atual.proximo;
        }

        noavaliacaousuario novo =
            new noavaliacaousuario(livrorecebido, nota);

        novo.proximo = avaliacoes;

        avaliacoes = novo;
    }
}
