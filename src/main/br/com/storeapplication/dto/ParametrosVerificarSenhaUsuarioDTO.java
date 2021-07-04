package br.com.storeapplication.dto;

public class ParametrosVerificarSenhaUsuarioDTO {

    private Integer idUsuario;
    private String senhaAtual;

    public ParametrosVerificarSenhaUsuarioDTO(Integer idUsuario, String senhaAtual) {
        this.idUsuario = idUsuario;
        this.senhaAtual = senhaAtual;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }
}