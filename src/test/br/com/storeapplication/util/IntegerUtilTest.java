package br.com.storeapplication.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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

}
