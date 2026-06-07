package br.com.storeapplication.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class StringUtilTest {

    @Test
    public void testarRetirarCaracteresEspeciaisDeCpfFormatado(){
        assertThat (StringUtil.retirarCaracteresEspeciais("123.456.789-00"), is("12345678900"));
    }

    @Test
    public void testarRetirarCaracteresEspeciaisDeStringVazia(){
        assertThat (StringUtil.retirarCaracteresEspeciais(""), is(""));
    }

    @Test
    public void testarRetirarCaracteresEspeciaisDeStringSemCaracteresEspeciais(){
        assertThat (StringUtil.retirarCaracteresEspeciais("12345678900"), is("12345678900"));
    }

    @Test
    public void testarRetirarCaracteresEspeciaisDeStringSomenteComCaracteresEspeciais(){
        assertThat (StringUtil.retirarCaracteresEspeciais(".-/ ()"), is(""));
    }

    @Test
    public void testarRetirarCaracteresEspeciaisDeStringComLetrasENumeros(){
        assertThat (StringUtil.retirarCaracteresEspeciais("Telefone: (11) 9-8765-4321"), is("11987654321"));
    }

    @Test
    public void testarRetirarCaracteresEspeciaisDeValorNuloLancaExcecao(){
        assertThrows(NullPointerException.class, () -> StringUtil.retirarCaracteresEspeciais(null));
    }

}
