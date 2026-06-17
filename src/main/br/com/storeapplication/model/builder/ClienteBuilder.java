package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.Cliente;
import br.com.storeapplication.shared.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class ClienteBuilder implements Builder {

    private Cliente cliente;

    public ClienteBuilder() {
        this.cliente = new Cliente();
    }

    public ClienteBuilder comId(Integer id) {
        cliente.setId(id);
        return this;
    }

    public ClienteBuilder comNome(String nome){
        cliente.setNome(nome);
        return this;
    }

    public ClienteBuilder comDataNascimento(Date dataNascimento){
        cliente.setDataNascimento(dataNascimento);
        return this;
    }

    public ClienteBuilder comCpf(String cpf){
        cliente.setCpf(cpf);
        return this;
    }

    public ClienteBuilder comRg(String rg){
        cliente.setRg(rg);
        return this;
    }

    public ClienteBuilder comTelefone1(String telefone1) {
        cliente.setTelefone1(telefone1);
        return this;
    }

    public ClienteBuilder comTelefone2(Integer telefone2) {
        cliente.setTelefone2(telefone2);
        return this;
    }

    public Cliente construir() {
        return this.cliente;
    }

    public Cliente mapear(ResultSet rs) throws SQLException {
        return this
                .comId(rs.getInt("id"))
                .comNome(rs.getString("nome"))
                .comDataNascimento(rs.getDate("data_nascimento"))
                .comCpf(rs.getString("cpf"))
                .comRg(rs.getString("rg"))
                .comTelefone1(rs.getString("telefone1"))
                .comTelefone2(rs.getInt("telefone2"))
                .construir();
    }
}
