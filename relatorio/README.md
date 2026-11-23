# trabalho prático: tabelas hash — amazon books reviews

projeto acadêmico desenvolvido em java para processar avaliações de livros, implementar três funções hash com encadeamento separado e identificar, para cada usuário, o livro com maior média global entre aqueles que ele avaliou.

## 1. objetivo

o programa processa a base de avaliações da amazon e atende aos requisitos funcionais do enunciado:

- rf1: implementar as funções hash fnv-1a, polinomial de base 31 e djb2.
- rf2: implementar uma tabela hash própria, com encadeamento separado e capacidade fixa.
- rf3: armazenar a soma e a quantidade de avaliações de cada título, além dos dados de cada usuário e dos livros que ele avaliou.
- rf4: descartar e contabilizar registros sem identificador de usuário, sem título ou com nota inválida. registros com nome de perfil vazio não devem ser descartados.
- rf5: gerar o arquivo `usuario_melhor_livro.csv`.
- rf6: consultar os identificadores listados em `dados/user_id.txt` e apresentar o nome de perfil e o melhor livro de cada usuário encontrado.
- rf7: gerar o ranking dos dez livros com maior média global, considerando somente títulos com pelo menos cem avaliações.
- rf8: medir o tempo de leitura e indexação, cálculo do melhor livro, ordenação, gravação dos arquivos e consultas.
- rf9: gerar a lista completa de usuários ordenada conforme os critérios do enunciado.

## 2. tecnologias e ambiente

- linguagem: java.
- ambiente utilizado no desenvolvimento: visual studio code e powershell no windows.
- compilação: compilador java (`javac`).
- execução: máquina virtual java (`java`).
- arquivos de entrada e saída no formato csv e texto.

a base completa é lida sequencialmente. entretanto, os dados indexados e as listas encadeadas permanecem em memória durante o processamento. por isso, a execução pode exigir uma quantidade considerável de memória ram.

## 3. estrutura do projeto

```text
trabalhohashamazon/
├── dados/
│   ├── amostra.csv
│   ├── books_rating.csv
│   └── user_id.txt
├── src/
│   ├── arquivos.java
│   ├── execucaocompleta.java
│   ├── experimentoshash.java
│   ├── funchash.java
│   ├── leitorcsv.java
│   ├── noavaliacaousuario.java
│   ├── nohash.java
│   ├── nolivro.java
│   ├── nousuario.java
│   ├── ordenacao.java
│   ├── processamento.java
│   ├── tabelahash.java
│   ├── tabelalivros.java
│   ├── tabelausuarios.java
│   └── classes de teste, caso ainda estejam nesta pasta
├── saidas/
│   ├── usuario_melhor_livro.csv
│   ├── usuario_melhor_livro_ordenado.csv
│   └── ranking_livros.csv
├── relatorio/
├── bin/
├── .gitignore
└── readme.md
```

a estrutura acima apresenta os diretórios principais. os arquivos de teste podem permanecer em `src`, como no desenvolvimento atual, ou ser movidos para uma pasta separada chamada `testes`. essa separação é opcional e não é uma exigência do enunciado. caso os testes sejam movidos, eles devem continuar no pacote padrão, sem declaração `package`, e a pasta precisa ser incluída nas raízes de código do projeto no vs code.

## 4. arquivos de entrada

### base completa

coloque o arquivo original `books_rating.csv` dentro da pasta `dados`. mantenha o nome e a capitalização exatamente iguais aos utilizados no código de processamento, especialmente em ambientes que diferenciam letras maiúsculas e minúsculas.

a base utilizada na execução registrada contém três milhões de registros e aproximadamente 2,86 gb. por causa do tamanho, o arquivo original não deve ser enviado ao repositório github. ele deve ser obtido separadamente e colocado na pasta indicada.

### amostra para testes

`dados/amostra.csv` é uma base reduzida utilizada nos testes do leitor csv, da validação dos registros, das tabelas e do processamento. a amostra não substitui a execução com a base completa exigida para a entrega.

### identificadores para consulta

`dados/user_id.txt` deve conter um identificador de usuário por linha. para validar consultas bem-sucedidas, utilize identificadores que realmente existam na base completa. também é possível acrescentar um identificador inexistente para verificar o comportamento de uma busca sem resultado.

## 5. regras de implementação do professor

a implementação deve respeitar as restrições do enunciado:

- utilizar somente os tipos numéricos e de caractere permitidos pelo enunciado, além do tipo textual da linguagem e de vetores desses tipos.
- não utilizar classes empacotadoras, como `integer`, `double`, `float` e `character`.
- não utilizar métodos prontos, exceto os permitidos para leitura e escrita de arquivos e para a medição de tempo.
- escrever em português os nomes das classes, métodos, variáveis e comentários do código.
- implementar manualmente as estruturas, comparações, funções hash, contagens e algoritmos solicitados.
- não utilizar o atributo `length` dos vetores.

entre os recursos expressamente proibidos pelo enunciado estão `hashmap`, `arraylist`, `string.hashcode`, `string.equals`, `double.parsedouble`, `integer.parseint`, `arrays.sort`, `arrays.copyof`, `system.arraycopy` e recursos da classe `math`.

a tabela hash deve ser própria, utilizar encadeamento separado e manter capacidade fixa de 1.000.003 posições. não há rehash: quando duas ou mais chaves calculam a mesma posição, elas são armazenadas na lista encadeada daquela posição.

**atenção à representação dos nós:** o projeto utiliza classes próprias para representar os nós das listas encadeadas. como o enunciado também limita os tipos permitidos, é necessário confirmar com o professor se as referências a essas classes próprias e os vetores de nós utilizados na implementação estão de acordo com a interpretação adotada na avaliação. este ponto não deve ser tratado como aprovado sem essa confirmação.

## 6. funções hash e tabela

foram implementadas três funções hash, aplicadas às mesmas chaves e utilizando a mesma estratégia de colisão:

- fnv-1a: valor inicial `0x811c9dc5`; para cada caractere, aplicar xor e multiplicar por `16777619`.
- polinomial de base 31: iniciar em zero e, para cada caractere, multiplicar o valor atual por 31 e somar o código do caractere.
- djb2: iniciar em `5381` e, para cada caractere, multiplicar o valor atual por 33 e somar o código do caractere.

para obter uma posição válida na tabela, utiliza-se a expressão `(h & 0x7fffffff) % 1000003`. o tamanho válido da chave é recebido separadamente, sem utilizar o atributo `length` do vetor.

a tabela utiliza encadeamento separado. a inserção e a busca percorrem a lista da posição calculada e comparam as chaves caractere por caractere. as coleções prontas do java não são utilizadas.

na parte experimental, cada configuração é executada com as chaves distintas de usuários e, separadamente, com as chaves distintas de títulos. a contagem de colisões considera uma colisão quando a lista da posição já está ocupada no momento da inserção. as sondagens contam os elementos examinados nas listas. a busca sem sucesso utiliza uma chave acrescida de um caractere de código 1, conforme o enunciado. os tempos de inserção e busca são medidos em três execuções, e a média dos tempos é apresentada.

## 7. compilação no windows

execute os comandos no powershell, a partir da pasta principal do projeto.

crie a pasta de saída da compilação:

```powershell
new-item -itemtype directory -force .\bin | out-null
```

compile as classes que estiverem em `src`:

```powershell
$fontes = get-childitem .\src -filter *.java | foreach-object { $_.fullname }
javac -encoding utf-8 -d .\bin $fontes
```

se os arquivos de teste forem movidos para uma pasta chamada `testes`, compile-os depois das classes de produção:

```powershell
$testes = get-childitem .\testes -filter *.java | foreach-object { $_.fullname }
javac -encoding utf-8 -cp .\bin -d .\bin $testes
```

se os testes permanecerem em `src`, não é necessário executar o segundo bloco: o primeiro comando compila todos os arquivos java dessa pasta.

## 8. execução do processamento completo

a classe `execucaocompleta` é o ponto de entrada para processar a base completa. execute:

```powershell
java -cp .\bin execucaocompleta
```

a execução completa realiza a leitura e a indexação dos registros, calcula o melhor livro de cada usuário, ordena os resultados, grava os arquivos csv, consulta os identificadores de `dados/user_id.txt` e executa os experimentos das funções hash.

as classes de teste devem ser executadas individualmente, depois da compilação. exemplos:

```powershell
java -cp .\bin funchash_teste
java -cp .\bin testetabelahash
java -cp .\bin testetabelalivros
java -cp .\bin testetabelausuarios
java -cp .\bin testeprocessamento
java -cp .\bin testeexperimentos
```

os testes que utilizam `amostra.csv` devem ser iniciados a partir da pasta principal do projeto, pois os caminhos dos arquivos são relativos a essa pasta.

a classe `main` é um teste independente do leitor csv com a amostra. ela não substitui `execucaocompleta` e não deve ser utilizada como ponto de entrada para o processamento integral da base.

## 9. arquivos gerados

a execução completa gera três arquivos na pasta `saidas`:

### `usuario_melhor_livro.csv`

contém uma linha por usuário, com o identificador do usuário, o nome de perfil, o título do melhor livro, a média global desse título, a quantidade total de avaliações do título e a maior nota que o próprio usuário deu a esse livro.

### `usuario_melhor_livro_ordenado.csv`

contém os mesmos dados do arquivo anterior, com todos os usuários ordenados pelos critérios do enunciado:

1. média global do livro em ordem decrescente;
2. quantidade de avaliações do livro em ordem decrescente;
3. identificador do usuário em ordem alfabética crescente.

a ordenação é realizada por um algoritmo próprio de merge sort, sem utilizar métodos prontos de ordenação.

### `ranking_livros.csv`

contém até dez títulos com as maiores médias globais, considerando somente livros com pelo menos cem avaliações.

## 10. validações e decisões do processamento

o programa considera inválidos os registros sem identificador de usuário, sem título ou com nota fora do intervalo permitido. um registro pode apresentar mais de um desses problemas; por isso, a soma dos contadores de motivos de descarte pode ser maior que o total de registros descartados. nomes de perfil vazios não são motivo para descarte.

a média utilizada para escolher o melhor livro é global, ou seja, considera todas as avaliações válidas daquele título na base. em caso de empate entre médias, a implementação documenta um critério determinístico: escolhe o título em ordem alfabética crescente.

para cada combinação de usuário e livro, a nota individual considerada é a maior nota que aquele usuário atribuiu ao título. o enunciado determina que, se o mesmo usuário aparecer com nomes diferentes, deve prevalecer o primeiro nome de perfil não vazio encontrado; se todos estiverem vazios, o campo permanece vazio. essa regra também deve ser conferida na validação final.

o leitor csv trata campos entre aspas, aspas duplicadas, vírgulas e quebras de linha dentro de campos. se encontrar um registro estruturalmente malformado, a versão atual interrompe a leitura em vez de tentar continuar a partir de um ponto possivelmente incorreto do arquivo.

a rotina de consulta foi revisada para remover espaços externos e um possível marcador bom no início do identificador. essa revisão ainda precisa ser validada com consultas bem-sucedidas utilizando identificadores reais da base completa.

## 11. resultados de uma execução completa registrada

os resultados abaixo correspondem a uma execução realizada durante o desenvolvimento, e não devem ser considerados uma auditoria final dos três arquivos csv.

| indicador | resultado |
|---|---:|
| registros lidos | 3.000.000 |
| registros válidos | 2.438.018 |
| registros descartados | 561.982 |
| registros sem identificador de usuário | 561.787 |
| registros sem título | 208 |
| notas inválidas | 0 |
| livros distintos | 206.711 |
| usuários distintos | 1.008.961 |

os contadores de registros sem identificador e sem título podem se sobrepor. por isso, a soma desses motivos não precisa ser igual ao total de registros descartados.

### tempos registrados

| etapa | tempo |
|---|---:|
| leitura e indexação | 70.467,9285 ms |
| cálculo do melhor livro | 88,201 ms |
| ordenação | 969,0897 ms |
| gravação dos arquivos | 3.195,5352 ms |
| consultas | 2,2188 ms |

na execução que gerou esses tempos, o arquivo de consulta ainda continha identificadores fictícios da amostra. todos foram informados como não encontrados na base completa. portanto, o tempo de consultas não comprova o funcionamento de buscas reais bem-sucedidas. além disso, essa medição ocorreu antes da revisão que remove espaços externos e marcador bom dos identificadores.

### experimentos com identificadores de usuários

| função | colisões | sondagens com sucesso | sondagens sem sucesso | pior caso | inserção média (ms) | busca média (ms) |
|---|---:|---:|---:|---:|---:|---:|
| fnv-1a | 373.603 | 1,5038 | 1,0076 | 8 | 49,0530 | 43,7557 |
| polinomial 31 | 373.726 | 1,5049 | 1,0082 | 9 | 135,8966 | 164,2848 |
| djb2 | 373.726 | 1,5042 | 1,0106 | 8 | 153,3675 | 159,7707 |

### experimentos com títulos

| função | colisões | sondagens com sucesso | sondagens sem sucesso | pior caso | inserção média (ms) | busca média (ms) |
|---|---:|---:|---:|---:|---:|---:|
| fnv-1a | 20.130 | 1,1042 | 0,2064 | 5 | 85,2306 | 27,3035 |
| polinomial 31 | 20.001 | 1,1035 | 0,2070 | 5 | 29,5616 | 42,0188 |
| djb2 | 19.895 | 1,1032 | 0,2082 | 5 | 28,7039 | 36,2785 |

os tempos da tabela são médias de três execuções. as demais medidas foram obtidas com as mesmas chaves distintas e não dependem do tempo de execução. nessa execução, fnv-1a apresentou menos colisões para usuários, enquanto djb2 apresentou menos colisões para títulos. a função com menos colisões não é necessariamente a mais rápida em todas as medições.

## 12. verificações pendentes antes da entrega

- atualizar `dados/user_id.txt` com identificadores existentes na base completa e repetir as consultas do rf6.
- conferir os cabeçalhos, a codificação, a quantidade de linhas e o conteúdo dos três arquivos csv gerados.
- verificar a ordenação global de todos os usuários no arquivo `usuario_melhor_livro_ordenado.csv`, incluindo os critérios de desempate.
- verificar se `ranking_livros.csv` contém somente títulos com pelo menos cem avaliações e está em ordem decrescente de média.
- confirmar com o professor a interpretação das referências e dos vetores de nós próprios utilizados no encadeamento separado.
- conferir se a regra do primeiro nome de perfil não vazio está sendo respeitada para identificadores repetidos.
- registrar os tempos da execução final e atualizar este readme e o relatório, caso os resultados mudem.
- inserir os endereços reais do repositório público e do vídeo de apresentação.

## 13. entregáveis e links

o enunciado exige um repositório público com histórico de commits, o relatório, os arquivos de saída gerados com a base completa e um vídeo de apresentação de até cinco minutos. o vídeo deve explicar a modelagem, mostrar a página inicial do repositório, apresentar as principais dificuldades e comentar os resultados e as limitações da solução.

**link do github:** a preencher com o endereço real do repositório público.

**link do vídeo:** a preencher com o endereço real do vídeo de apresentação.

## 14. arquivos que não devem ser enviados ao github

não envie a base original completa, devido ao seu tamanho. uma configuração inicial para `.gitignore` pode conter:

```gitignore
/bin/
/dados/*
!/dados/amostra.csv
!/dados/user_id.txt
```

os três arquivos csv de saída são entregáveis do trabalho e devem ser preservados para a entrega, conforme as orientações do professor.
