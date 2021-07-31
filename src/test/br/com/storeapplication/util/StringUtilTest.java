package br.com.storeapplication.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class StringUtilTest {

    @Test
    public void testarRetirarCaracteresEspeciais(){
        assertThat (StringUtil.retirarCaracteresEspeciais("123.456.789-00"), is("12345678900"));
    }

}
