# Regras de segurança

Estas regras têm prioridade sobre conveniência ou velocidade. Em caso de dúvida, pare e
pergunte ao usuário em vez de assumir.

1. **Nunca execute comandos que alterem a estrutura ou os dados do banco de dados**
   (`psql`, `DROP`, `DELETE`, `UPDATE`, `INSERT`, migrações, etc.) sem confirmação
   explícita do usuário — mesmo em ambiente local.

2. **Nunca execute comandos que alterem o repositório** (commits, criação/remoção de
   branches, tags, etc.) **sem confirmar antes** com o usuário, a menos que ele já tenha
   pedido explicitamente essa ação na própria mensagem.

3. **Nunca exponha dados sensíveis** (credenciais, senhas, tokens, URLs de conexão de
   produção, variáveis de ambiente `STORE_DB_URL_*`/`DB_USUARIO_*`/`DB_SENHA_*`) em
   commits, mensagens de PR, logs ou qualquer saída visível.

4. **Nunca faça `git push` na branch `main` sem perguntar antes.** Se o objetivo é abrir
   um PR, sempre crie uma branch nova primeiro e abra o PR contra `main`.

5. **`Propriedades.Conexao` deve estar em `PRODUCAO` antes de qualquer commit.** Nunca
   deixar `LOCALHOST` ou `DEPLOY` commitado em
   `src/main/br/com/storeapplication/factory/Propriedades.java` — reverter para
   `PRODUCAO` antes de finalizar qualquer mudança (ver skill `switch-env`).

6. **Operações destrutivas de git exigem confirmação explícita antes de executar**:
   `git push --force`/`--force-with-lease`, `git reset --hard`, `git clean -f`/`-d`,
   `git checkout -- .`/`git restore .`, `--no-verify`, `--no-gpg-sign`, `branch -D`.
   Nunca usá-las como atalho para "resolver" um problema — investigar a causa raiz
   primeiro.
