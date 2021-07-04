package br.com.storeapplication.model.builder;

import br.com.storeapplication.model.Endereco;
import br.com.storeapplication.shared.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;


public class EnderecoBuilder implements Builder {

    private Endereco endereco;

    public EnderecoBuilder() {
        this.endereco = new Endereco();
    }

    public EnderecoBuilder comCep(String cep){
        endereco.setCep(cep);
        return this;
    }

    public EnderecoBuilder comEstado(String estado){
        endereco.setEstado(estado);
        return this;
    }

    public EnderecoBuilder comCidade(String cidade){
        endereco.setCidade(cidade);
        return this;
    }

    public EnderecoBuilder comBairro(String bairro){
        endereco.setBairro(bairro);
        return this;
    }

    public EnderecoBuilder comLogradouro(String logradouro){
        endereco.setLogradouro(logradouro);
        return this;
    }

    public EnderecoBuilder comNumero(Integer numero){
        endereco.setNumero(numero);
        return this;
    }

    public EnderecoBuilder comCodIBGE(Integer codIBGE){
        endereco.setCodIBGE(codIBGE);
        return this;
    }

    public Endereco construir() {
        return this.endereco;
    }

    public Endereco mapear(ResultSet rs) throws SQLException {
        return this
                .comCep(rs.getString("cep"))
                .comEstado(rs.getString("estado"))
                .comCidade(rs.getString("cidade"))
                .comBairro(rs.getString("bairro"))
                .comLogradouro(rs.getString("logradouro"))
                .comNumero(rs.getInt("numero"))
                .comCodIBGE(rs.getInt("cod_ibge"))
                .construir();
    }
}
