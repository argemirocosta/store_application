package br.com.storeapplication.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Cliente implements Serializable {

    private Integer id;
    private String nome;
    private Date dataNascimento;
    private String cpf;
    private String rg;
    private String telefone1;
    private Integer telefone2;

    public Cliente() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public Integer getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(Integer telefone2) {
        this.telefone2 = telefone2;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id) &&
                Objects.equals(nome, cliente.nome) &&
                Objects.equals(dataNascimento, cliente.dataNascimento) &&
                Objects.equals(cpf, cliente.cpf) &&
                Objects.equals(rg, cliente.rg) &&
                Objects.equals(telefone1, cliente.telefone1) &&
                Objects.equals(telefone2, cliente.telefone2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, dataNascimento, cpf, rg, telefone1, telefone2);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", cpf='" + cpf + '\'' +
                ", rg='" + rg + '\'' +
                ", telefone1=" + telefone1 +
                ", telefone2=" + telefone2 +
                '}';
    }
}