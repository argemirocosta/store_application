package br.com.storeapplication.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.hamcrest.CoreMatchers.*;


public class IntegerUtilTest {

    @Test
    public void testarTratamentoValorVindoZeroComValor(){
        assertThat (IntegerUtil.tratarValorVindoZero("0"), is(0));
    }

    @Test
    public void testarTratamentoValorVindoZeroComNulo(){
        assertThat (IntegerUtil.tratarValorVindoZero(null), is(nullValue()));
    }

    @Test
    public void testarTratamentoValorVindoZeroComStringVazia(){
        assertThat (IntegerUtil.tratarValorVindoZero(""), is(nullValue()));
    }

    @Test
    public void testarTratamentoValorVindoZeroComValorPositivo(){
        assertThat (IntegerUtil.tratarValorVindoZero("42"), is(42));
    }

    @Test
    public void testarTratamentoValorVindoZeroComValorNegativo(){
        assertThat (IntegerUtil.tratarValorVindoZero("-7"), is(-7));
    }

    @Test
    public void testarTratamentoValorVindoZeroComValorNaoNumericoLancaExcecao(){
        assertThrows(NumberFormatException.class, () -> IntegerUtil.tratarValorVindoZero("abc"));
    }

}
