package br.com.storeapplication.builders;

import br.com.storeapplication.model.Usuario;

public class UsuarioBuilderTest {

    private Usuario usuario;

    private UsuarioBuilderTest(){

    }

    public static UsuarioBuilderTest usuarioTesteInserir(){
        UsuarioBuilderTest builderTest = new UsuarioBuilderTest();
        builderTest.usuario = new Usuario();
        builderTest.usuario.setNome("Usuario teste 1");
        builderTest.usuario.setLogin("usuario.teste1");
        builderTest.usuario.setSenha("123456");
        builderTest.usuario.setAtivo(true);
        return builderTest;
    }

    public Usuario agora(){
        return usuario;
    }

}
