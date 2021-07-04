package br.com.storeapplication.service;

import br.com.storeapplication.dao.UsuarioDAO;
import br.com.storeapplication.dto.ParametrosVerificarSenhaUsuarioDTO;
import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.model.Usuario;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        usuarioDAO = new UsuarioDAO();
    }

    public Usuario login(Usuario usuario) {
        return usuarioDAO.login(usuario);
    }

    public void inserirUsuario(Usuario usuario) throws ProjetoException {
        usuarioDAO.inserirUsuario(usuario);
    }

    public Boolean verificarSenhaUsuario(ParametrosVerificarSenhaUsuarioDTO parametrosVerificarSenhaUsuarioDTO) {
        return usuarioDAO.verificarSenhaUsuario(parametrosVerificarSenhaUsuarioDTO);
    }

    public void alterarUsuario(Usuario usuario) throws ProjetoException {
        usuarioDAO.alterarUsuario(usuario);
    }
}