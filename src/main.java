import java.io.IOException;

public class main {

    // nomes esperados no cabecalho
    private static final char[] cabecalho_titulo =
        {'T', 'i', 't', 'l', 'e'};

    private static final char[] cabecalho_usuario =
        {'U', 's', 'e', 'r', '_', 'i', 'd'};

    private static final char[] cabecalho_perfil =
        {'p', 'r', 'o', 'f', 'i', 'l', 'e', 'N', 'a', 'm', 'e'};

    private static final char[] cabecalho_nota =
        {'r', 'e', 'v', 'i', 'e', 'w', '/', 's', 'c', 'o', 'r', 'e'};


    public static void main(String[] principal) {

        String caminho = "dados/amostra.csv";

        leitorcsv leitor = new leitorcsv();

        int registroslidos = 0;
        int registrosvalidos = 0;
        int registrosdescartados = 0;

        int semusuario = 0;
        int semtitulo = 0;
        int notainvalida = 0;

        int errocsv = 0;

        try {

            leitor.abrir(caminho);

            // le o cabecalho
            int retorno = leitor.lerRegistro();

            if (retorno != 1 ||
                leitor.obterQuantidadeColunas() != 10) {

                System.out.println(
                    "erro: cabecalho invalido ou com quantidade incorreta de colunas."
                );

                return;
            }

            // confere se as colunas estao nas posicoes certas
            if (
                compararCampo(
                    leitor.obterTitulo(),
                    leitor.obterTamanhoTitulo(),
                    cabecalho_titulo,
                    5
                ) != 1 ||

                compararCampo(
                    leitor.obterUsuario(),
                    leitor.obterTamanhoUsuario(),
                    cabecalho_usuario,
                    7
                ) != 1 ||

                compararCampo(
                    leitor.obterPerfil(),
                    leitor.obterTamanhoPerfil(),
                    cabecalho_perfil,
                    11
                ) != 1 ||

                compararCampo(
                    leitor.obterNotaTexto(),
                    leitor.obterTamanhoNotaTexto(),
                    cabecalho_nota,
                    12
                ) != 1
            ) {

                System.out.println(
                    "erro: nomes das colunas em posicoes inesperadas."
                );

                return;
            }

            System.out.println("cabecalho reconhecido corretamente.");

            System.out.println(
                "title: coluna 1 | user_id: coluna 3"
            );

            System.out.println(
                "profilename: coluna 4 | review/score: coluna 6"
            );

            System.out.println();


            // le os registros de dados
            while (true) {

                retorno = leitor.lerRegistro();

                if (retorno == 0) {
                    break;
                }

                if (retorno == -1) {

                    System.out.println(
                        "erro: registro csv malformado ou com quantidade diferente de 10 colunas."
                    );

                    errocsv = 1;
                    break;
                }

                registroslidos++;


                // ve se o usuario esta vazio
                int faltausuario = estaVazio(
                    leitor.obterUsuario(),
                    leitor.obterTamanhoUsuario()
                );


                // ve se o titulo esta vazio
                int faltatitulo = estaVazio(
                    leitor.obterTitulo(),
                    leitor.obterTamanhoTitulo()
                );


                // converte a nota sem usar parsedouble
                double nota = converterNota(
                    leitor.obterNotaTexto(),
                    leitor.obterTamanhoNotaTexto()
                );


                // ve se a nota esta entre 1.0 e 5.0
                int faltanota = 0;

                if (nota < 1.0 || nota > 5.0) {
                    faltanota = 1;
                }


                // conta cada tipo de erro
                if (faltausuario == 1) {
                    semusuario++;
                }

                if (faltatitulo == 1) {
                    semtitulo++;
                }

                if (faltanota == 1) {
                    notainvalida++;
                }


                // descarta se tiver algum problema
                if (
                    faltausuario == 1 ||
                    faltatitulo == 1 ||
                    faltanota == 1
                ) {

                    registrosdescartados++;
                    continue;
                }


                // registro valido
                registrosvalidos++;

                System.out.println(
                    "registro valido " + registrosvalidos
                );

                System.out.print("user_id: ");

                imprimirCampo(
                    leitor.obterUsuario(),
                    leitor.obterTamanhoUsuario()
                );

                System.out.print("title: ");

                imprimirCampo(
                    leitor.obterTitulo(),
                    leitor.obterTamanhoTitulo()
                );

                System.out.print("profilename: ");

                if (
                    estaVazio(
                        leitor.obterPerfil(),
                        leitor.obterTamanhoPerfil()
                    ) == 1
                ) {

                    System.out.println("(vazio - registro mantido)");

                } else {

                    imprimirCampo(
                        leitor.obterPerfil(),
                        leitor.obterTamanhoPerfil()
                    );
                }

                System.out.println("nota: " + nota);
                System.out.println();
            }


            // mostra o resumo
            System.out.println("===== resumo =====");

            System.out.println(
                "registros de dados lidos: " + registroslidos
            );

            System.out.println(
                "registros validos: " + registrosvalidos
            );

            System.out.println(
                "registros descartados: " + registrosdescartados
            );

            System.out.println(
                "sem user_id: " + semusuario
            );

            System.out.println(
                "sem title: " + semtitulo
            );

            System.out.println(
                "nota invalida: " + notainvalida
            );

            if (errocsv == 1) {
                System.out.println(
                    "leitura interrompida por erro de formatacao."
                );
            }

        } catch (IOException erro) {

            System.out.println(
                "erro ao acessar o arquivo. verifique o caminho e as permissoes."
            );

        } finally {

            try {
                leitor.fechar();
            } catch (IOException errofechamento) {
                System.out.println(
                    "nao foi possivel fechar o arquivo corretamente."
                );
            }
        }
    }


    // compara dois vetores letra por letra
    private static int compararCampo(
        char[] recebido,
        int tamanhorecebido,
        char[] esperado,
        int tamanhoesperado
    ) {

        if (tamanhorecebido != tamanhoesperado) {
            return 0;
        }

        for (int i = 0; i < tamanhorecebido; i++) {

            if (recebido[i] != esperado[i]) {
                return 0;
            }
        }

        return 1;
    }


    // ve se o campo so tem espacos ou quebras de linha
    private static int estaVazio(char[] campo, int tamanho) {

        for (int i = 0; i < tamanho; i++) {

            if (
                campo[i] != ' ' &&
                campo[i] != '\t' &&
                campo[i] != '\r' &&
                campo[i] != '\n'
            ) {

                return 0;
            }
        }

        return 1;
    }


    // transforma o texto da nota em double
    private static double converterNota(
        char[] texto,
        int tamanho
    ) {

        int inicio = 0;
        int fim = tamanho - 1;

        // tira espacos do comeco e do fim
        while (
            inicio <= fim &&
            (texto[inicio] == ' ' || texto[inicio] == '\t')
        ) {
            inicio++;
        }

        while (
            fim >= inicio &&
            (texto[fim] == ' ' || texto[fim] == '\t')
        ) {
            fim--;
        }

        if (inicio > fim) {
            return -1.0;
        }


        int sinal = 1;

        if (texto[inicio] == '-') {
            sinal = -1;
            inicio++;

        } else if (texto[inicio] == '+') {
            inicio++;
        }


        double valor = 0.0;
        double fatordecimal = 0.1;

        int encontrouDigito = 0;
        int encontrouPonto = 0;


        for (int i = inicio; i <= fim; i++) {

            char caractere = texto[i];

            if (caractere >= '0' && caractere <= '9') {

                encontrouDigito = 1;

                int digito = caractere - '0';

                if (encontrouPonto == 0) {

                    valor = valor * 10.0 + digito;

                } else {

                    valor = valor + digito * fatordecimal;

                    fatordecimal = fatordecimal / 10.0;
                }

            } else if (
                caractere == '.' &&
                encontrouPonto == 0
            ) {

                encontrouPonto = 1;

            } else {

                return -1.0;
            }
        }


        if (encontrouDigito == 0) {
            return -1.0;
        }

        valor = valor * sinal;

        if (valor < 1.0 || valor > 5.0) {
            return -1.0;
        }

        return valor;
    }


    // imprime o vetor caractere por caractere
    private static void imprimirCampo(
        char[] campo,
        int tamanho
    ) {

        for (int i = 0; i < tamanho; i++) {
            System.out.print(campo[i]);
        }

        System.out.println();
    }
}