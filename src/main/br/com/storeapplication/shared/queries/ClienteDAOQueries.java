package br.com.storeapplication.shared.queries;

public class ClienteDAOQueries {

    private ClienteDAOQueries() {
    }

    public static final String SELECT_LISTAR_CLIENTES = "SELECT id, nome, data_nascimento, cpf, rg, telefone1, telefone2, "
            + "cep, estado, cidade, bairro, logradouro, numero, cod_ibge "
            + "FROM vendas.clientes "
            + "WHERE usuario = ? ORDER BY nome";

    public static final String SELECT_BUSCAR_CLIENTE_POR_NOME_OU_TELEFONE = "SELECT id, nome, data_nascimento, cpf, rg, telefone1, telefone2, "
            + "cep, estado, cidade, bairro, logradouro, numero, cod_ibge "
            + "FROM vendas.clientes "
            + "WHERE (nome ILIKE ? OR telefone1 ILIKE ?) AND usuario = ? ORDER BY nome";

    public static final String SELECT_BUSCAR_CLIENTE_POR_ID = "SELECT id, nome, data_nascimento, cpf, rg, telefone1, telefone2, "
            + "cep, estado, cidade, bairro, logradouro, numero, cod_ibge "
            + "FROM vendas.clientes "
            + "WHERE id = ? AND usuario = ? ORDER BY nome";

    public static final String INSERIR_CLIENTE = "INSERT INTO vendas.clientes (nome, telefone1, telefone2, usuario, data_nascimento, cpf, rg, "
            + "cep, estado, cidade, bairro, logradouro, numero, cod_ibge) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING id";

    public static final String ALTERAR_CLIENTE = "UPDATE vendas.clientes SET nome=?, telefone1=?, telefone2=?, data_nascimento=?, cpf=?, rg=?, "
            + "cep=?, estado=?, cidade=?, bairro=?, logradouro=?, numero=?, cod_ibge=? "
            + "WHERE id=?";

    public static final String DELETAR_CLIENTE = "DELETE FROM vendas.clientes WHERE id=?";

}
