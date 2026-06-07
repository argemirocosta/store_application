package br.com.storeapplication.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class VerificadorUtilTest {

    @Test
    public void testarVerificarSeObjetoNuloComObjetoNulo(){
        assertThat (VerificadorUtil.verificarSeObjetoNulo(null), is(true));
    }

    @Test
    public void testarVerificarSeObjetoNuloComObjetoPreenchido(){
        assertThat (VerificadorUtil.verificarSeObjetoNulo("qualquer valor"), is(false));
    }

    @Test
    public void testarVerificarSeObjetoNuloOuVazioComObjetoNulo(){
        assertThat (VerificadorUtil.verificarSeObjetoNuloOuVazio(null), is(true));
    }

    @Test
    public void testarVerificarSeObjetoNuloOuVazioComStringVazia(){
        assertThat (VerificadorUtil.verificarSeObjetoNuloOuVazio(""), is(true));
    }

    @Test
    public void testarVerificarSeObjetoNuloOuVazioComStringPreenchida(){
        assertThat (VerificadorUtil.verificarSeObjetoNuloOuVazio("abc"), is(false));
    }

    @Test
    public void testarVerificarSeObjetoNuloOuVazioComObjetoNaoString(){
        assertThat (VerificadorUtil.verificarSeObjetoNuloOuVazio(Integer.valueOf(5)), is(false));
    }

}
