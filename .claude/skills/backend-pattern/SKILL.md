---
name: backend-pattern
description: Referência do padrão de código backend (model/builder/queries/dao/service/controller) usado neste projeto, para escrever ou revisar código Java seguindo as mesmas convenções
---

Esta skill descreve o padrão arquitetural e as convenções de código do backend do
StoreApplication. Use-a como checklist ao escrever código novo ou ao revisar um diff de
código Java em `src/main/br/com/storeapplication`. Todo código e mensagens são em
português (pt-BR).

Para cada entidade (ex: `Cliente`, `Venda`), o fluxo é:

```
controller (*MB) → service (*Service) → dao (*DAO) → PostgreSQL (JDBC puro)
        ↑                                    ↓
  model/builder (*Builder) ←──────── shared/queries (*DAOQueries)
```

## 1. `model` — classes de dados simples

POJOs com atributos privados e getters/setters públicos. Sem lógica de negócio, sem
anotações. Ex: `Cliente`, `Venda`, `Endereco`.

## 2. `dto` — objetos de transferência (`*DTO`)

Usado quando os dados não correspondem 1:1 a uma entidade de `model` (ex: resultado de
agregação/relatório) ou quando um método precisa agrupar vários parâmetros de
entrada. POJOs simples (`private` + getters/setters), sem builder e sem implementar
`shared.Builder`.

- **Resultado de consulta/relatório** (ex: `VendasComDescontoDTO`): construtor sem
  args; o DAO instancia `new XxxDTO()` e usa os setters para mapear o `ResultSet`
  diretamente dentro do `while (rs.next())`, sem passar por `model.builder`.
- **Agrupamento de parâmetros** (ex: `ParametrosVerificarSenhaUsuarioDTO`): construtor
  com todos os campos, usado para passar múltiplos valores de
  controller → service → dao em um único objeto em vez de vários parâmetros soltos.
- Nome sempre termina em `DTO` e a classe fica em `br.com.storeapplication.dto`.

## 3. `model.builder` — `*Builder implements Builder`

- Construtor sem args inicializa o objeto interno (`this.cliente = new Cliente()`).
- Métodos fluentes `comXxx(tipo valor)` fazem `objeto.setXxx(valor); return this;`.
- `construir()` retorna o objeto montado.
- `mapear(ResultSet rs) throws SQLException` lê colunas do `ResultSet` via os mesmos
  métodos `comXxx(...)`, encadeados, terminando em `.construir()`. Sub-objetos (ex:
  `Endereco`) são montados delegando para o builder deles: `new EnderecoBuilder().mapear(rs)`.

Ao adicionar uma coluna/campo novo: adicionar `comXxx` no builder e incluí-lo na cadeia de
`mapear`.

## 4. `shared.queries` — `*DAOQueries`

- Classe `final`/utilitária com construtor privado (`private XxxDAOQueries() {}`).
- Apenas constantes `public static final String`, sem lógica.
- Nomenclatura por prefixo de operação: `SELECT_LISTAR_XXX`, `SELECT_BUSCAR_XXX_POR_YYY`,
  `SELECT_VERIFICAR_XXX_CADASTRADO`, `INSERIR_XXX`, `ALTERAR_XXX`, `DELETAR_XXX`.
- `INSERIR_*` usa `RETURNING id` quando o id gerado precisa ser usado depois.
- SQL nunca aparece hardcoded dentro do DAO — sempre como constante aqui.

## 5. `dao` — acesso JDBC

Toda operação segue este esqueleto:

```java
public TipoRetorno operacao(...) {
    conexao = ConnectionFactory.getConnection();
    ...
    try {
        PreparedStatement ps = conexao.prepareStatement(NOME_DA_QUERY);
        ps.setX(1, valor);
        ...
    } catch (SQLException ex) {
        // leitura: apenas loga
        ex.printStackTrace();
        // escrita: propaga
        // throw new ProjetoException(ex);
    } finally {
        try {
            conexao.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    return retorno;
}
```

Pontos a verificar:

- **Conexão**: campo de instância `private Connection conexao = null;`, obtida via
  `ConnectionFactory.getConnection()` no início de cada método (cada método abre/fecha a
  própria conexão).
- **Leitura (SELECT)**: `catch (SQLException ex) { ex.printStackTrace(); }` — não lança
  exceção, retorna lista vazia/valor default já inicializado antes do `try`.
- **Escrita (INSERT/UPDATE/DELETE)**: assinatura `throws ProjetoException`;
  `catch (SQLException ex) { throw new ProjetoException(ex); }`; chama `conexao.commit()`
  ao final do `try`, antes de cair no `finally` (a conexão é manual, `autoCommit=false`).
- **`finally`**: sempre fecha a conexão, com seu próprio try/catch que apenas loga.
- **Escopo por usuário**: quando a entidade pertence ao usuário logado, busca
  `Usuario usuarioSessao = SessaoUtil.resgatarUsuarioDaSessao();` e filtra/insere com
  `usuario = ?` / `ps.setInt(n, usuarioSessao.getId())` (ver seção "Session &
  multi-tenancy" no `CLAUDE.md` — todo dado "do usuário" segue esse padrão).
- **Valores opcionais**: antes de `ps.setXxx`, checar com
  `VerificadorUtil.verificarSeObjetoNulo(...)` ou `verificarSeObjetoNuloOuVazio(...)`; se
  nulo/vazio, usar `ps.setNull(indice, Types.XXX)` no lugar do valor.
- **Mapeamento de listas**: método privado `mapearResultSetIniciarListaXxx(ResultSet rs)`
  itera `rs.next()` criando um `XxxBuilder` por linha e chamando `.mapear(rs)`.
- **Strings**: nomes geralmente salvos em upper-case (`cliente.getNome().toUpperCase()`);
  CPF/CEP passam por `StringUtil.retirarCaracteresEspeciais(...)` antes de persistir.

## 6. `service` — camada fina

- Um `*Service` por `*DAO`, instanciado no construtor (`private XxxDAO xxxDAO = new XxxDAO();`).
- Maioria dos métodos é repasse 1:1 (`return xxxDAO.metodo(args);` /
  `xxxDAO.metodo(args);`), preservando o `throws ProjetoException` quando o DAO lança.
- Regra de negócio que não é puramente acesso a dados (e não é responsabilidade da view)
  entra aqui — mas o padrão default é repasse direto. Não adicionar validação/lógica nova
  no service sem necessidade.

## 7. `controller` — `*MB`

```java
@ViewScoped
@ManagedBean
public class XxxMB {

    private Xxx xxx;
    private List<Xxx> listaXxx;
    private XxxService xxxService;

    public XxxMB() {
        xxx = new Xxx();
        xxxService = new XxxService();
    }

    public void inserirXxx() {
        try {
            xxxService.inserirXxx(xxx);
            limparCampos();
            listarXxx();
            JSFUtil.fecharDialog(DIALOG_CADASTRAR_XXX);
            JSFUtil.adicionarMensagemSucesso(XXX_CADASTRADO_SUCESSO, SUCESSO);
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(XXX_CADASTRADO_ERRO, ERRO);
        }
    }

    //GETTERS E SETTERS
    ...
}
```

Pontos a verificar:

- Anotado com `@ViewScoped @ManagedBean` (pacote `javax.faces.bean`).
- Estado: a entidade atual (`private Xxx xxx;`), a lista exibida (`private List<Xxx> listaXxx;`),
  campos de busca/filtro, e a instância do `*Service`.
- Construtor inicializa a entidade (`new Xxx()`) e o service.
- Métodos de ação que chamam operações de escrita (`inserir`/`alterar`/`deletar`) capturam
  `ProjetoException` e:
  - sucesso: `limparCampos()`, recarregar lista (`listarXxx()`), `JSFUtil.fecharDialog(...)`
    com uma constante de `shared.Dialogs`, e
    `JSFUtil.adicionarMensagemSucesso(MENSAGEM_SUCESSO, SUCESSO)`.
  - erro: `JSFUtil.adicionarMensagemErro(MENSAGEM_ERRO, ERRO)`.
  - As constantes de mensagem vêm de `shared.Mensagens` (import estático), seguindo o
    padrão `XXX_OPERACAO_SUCESSO` / `XXX_OPERACAO_ERRO`, descritas em pt-BR com `!`.
- `import static br.com.storeapplication.shared.Dialogs.*;` e
  `import static br.com.storeapplication.shared.Mensagens.*;` no topo.
- Getters e setters ficam agrupados ao final da classe, sob o comentário
  `//GETTERS E SETTERS`.

## 8. Testes de integração de negócio

Ao adicionar/alterar regra de negócio em um `*DAO` (cálculos, agregações, normalização,
escopo por usuário), considere cobrir com um teste `dao/*IT` (ex: `VendaDAOIT`,
`ClienteDAOIT`), que roda contra Postgres real via Testcontainers
(`mvn verify -P integration-tests`, requer Docker — ver seção "Tests" do `CLAUDE.md`).
Estendem `integration.PostgresIntegrationTestBase` e usam
`integration.UsuarioFixture.criarUsuario()` para isolar dados por teste.

## 9. Convenções gerais

- Nomes de classes, métodos, variáveis e comentários em português
  (`listarClientes`, `buscarClientePorNome`, `inserirCliente`, `alterarCliente`,
  `deletarCliente`, `verificarXxxCadastrado`).
- `exception.ProjetoException` é o único checked exception, usado apenas para encapsular
  `SQLException` em operações de escrita — nunca em leituras.
- Ao adicionar uma entidade ou campo novo, espera-se tocar: `model`, `model.builder`,
  `shared.queries`, `dao`, `service`, `controller` e a página `.xhtml` correspondente
  (ver skill `frontend-pattern`).
