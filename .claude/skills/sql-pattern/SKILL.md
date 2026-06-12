---
name: sql-pattern
description: Boas práticas e convenções de SQL (PostgreSQL/JDBC) usadas neste projeto em shared.queries.*DAOQueries, para escrever ou revisar queries seguindo o mesmo padrão
---

Esta skill descreve as convenções de SQL usadas no projeto, todas centralizadas em
`shared.queries.*DAOQueries` (ver skill `backend-pattern` para o papel dessa camada no
fluxo DAO). Use-a como checklist ao escrever ou revisar constantes SQL.

## 1. Onde o SQL vive

- SQL é sempre uma constante `public static final String` em
  `shared.queries.*DAOQueries`, nunca uma string montada/hardcoded dentro do DAO.
- Nome da constante segue o prefixo da operação: `SELECT_LISTAR_XXX`,
  `SELECT_BUSCAR_XXX_POR_YYY`, `SELECT_VERIFICAR_XXX_CADASTRADO`, `SELECT_CONSULTAR_*`
  (relatórios/agregações), `INSERIR_XXX`, `ALTERAR_XXX`, `DELETAR_XXX`.

## 2. Parametrização — nunca concatenar valores

- **Sempre** `PreparedStatement` com placeholders `?`; valores vêm exclusivamente via
  `ps.setXxx(indice, valor)`. Nunca concatenar variáveis/entrada do usuário diretamente
  na string SQL (risco de SQL injection).
- Buscas textuais usam `ILIKE` com wrapping de `%` feito no Java
  (`"%" + campoBusca + "%"`), nunca formatando o `%` dentro da constante SQL.
- Os índices dos `?` na constante devem corresponder exatamente, na ordem, às chamadas
  `ps.setX(n, ...)` no DAO — ao alterar uma query, revisar os dois lados juntos.

## 3. Schema e nomenclatura

- Toda tabela é referenciada com o schema explícito: `vendas.clientes`,
  `vendas.venda`, `vendas.usuario`, `vendas.forma_pagamento`, `vendas.estoque`.
- Tabelas e colunas em `snake_case` minúsculo, nomes em português
  (`data_nascimento`, `percentual_desconto`, `id_forma_pagamento`).
- Palavras reservadas usadas como nome de coluna (ex: `data`) são citadas com aspas
  duplas: `v."data"`.
- `SELECT` lista as colunas explicitamente, na mesma ordem em que o
  `*Builder.mapear(ResultSet)` (ou o DTO) as lê — nunca `SELECT *`.

## 4. Joins e aliases

- Alias curto de uma letra (ou poucas letras) por tabela, consistente entre queries:
  `v` = `venda`, `c` = `clientes`, `fp` = `forma_pagamento`, `e` = `estoque`.
- `JOIN ... ON (condição)` quando a relação é obrigatória (ex: venda → forma de
  pagamento); `LEFT JOIN` quando o relacionado pode não existir (ex: venda → cliente).

```sql
SELECT v.id, v.id_cliente, c.nome, v.data, v.valor, v.qtd,
       v.id_forma_pagamento, fp.descricao, desconto, percentual_desconto
FROM vendas.venda v
JOIN vendas.forma_pagamento fp ON (v.id_forma_pagamento = fp.id)
LEFT JOIN vendas.clientes c ON (v.id_cliente = c.id)
WHERE v.id_cliente = ? AND v.cancelada IS NOT TRUE
ORDER BY v.data DESC, v.id DESC
```

## 5. Flags booleanas e soft state

- Comparar flags com `IS TRUE` / `IS NOT TRUE` (não `= true`/`= false`), pois trata
  `NULL` corretamente quando a coluna é `bool NULL`: `cancelada IS NOT TRUE`,
  `desconto IS TRUE`, `fp.credito IS NOT TRUE`.
- "Exclusão"/cancelamento geralmente é lógico (`UPDATE ... SET cancelada = TRUE,
  data_hora_cancelamento = CURRENT_TIMESTAMP WHERE id = ?`), não `DELETE`. `DELETE` só é
  usado quando o domínio realmente remove o registro (ex: `DELETAR_CLIENTE`).

## 6. Escopo por usuário (multi-tenancy)

- Toda tabela com coluna `usuario` filtra/insere por `usuario = ?` usando o
  `usuarioSessao.getId()` (ver "Session & multi-tenancy" no `CLAUDE.md` e skill
  `backend-pattern`). Ao escrever uma nova query para dado do usuário, incluir essa
  condição.

## 7. INSERT/UPDATE

- `INSERT` lista colunas explicitamente e usa `RETURNING id` quando o DAO precisa do id
  gerado (`INSERT INTO vendas.clientes (...) VALUES (?,?,...) RETURNING id`).
- Colunas com valor fixo/derivado podem ser literais na própria query em vez de `?`
  quando não variam (ex: `INSERT INTO vendas.usuario (nome, login, senha, ativo)
  VALUES (?,?,?,TRUE)`).
- `UPDATE` usa `SET coluna = ?, coluna2 = ? WHERE id = ?` — a cláusula `WHERE id = ?` é
  sempre o último parâmetro.

## 8. Datas, agregações e subqueries

- Filtros de período usam `BETWEEN ? AND ?` ou `>= ? AND <= ?` sobre `"data"`; a
  conversão `java.util.Date` → `java.sql.Date` é feita no Java
  (`DataUtil.converterDateUtilParaDateSql`), não em SQL.
- Relatórios/cálculos agregados (`SELECT_CONSULTAR_*`) usam `sum(...)`, `count(...)`,
  `GROUP BY`, e subqueries escalares entre parênteses para compor valores (ex:
  cálculo de estoque combinando três subtotais). Cada subquery repete as mesmas
  condições de filtro (`usuario = ?`, `cancelada IS NOT TRUE`) que a query externa.
- Listagens para exibição (`SELECT_LISTAR_*`) sempre têm `ORDER BY` (ex:
  `ORDER BY nome`, `ORDER BY v.data DESC, v.id DESC`) para garantir ordem estável na UI.

## 9. Formatação da constante Java

- Strings longas são concatenadas com `+` (ou `"..."  + "..."`), uma cláusula lógica
  (`SELECT`, `FROM`, `JOIN`, `WHERE`, `GROUP BY`, `ORDER BY`) por linha/segmento,
  indentadas de forma alinhada para ficar legível como SQL.
