package br.com.storeapplication.util;

import org.junit.Test;

import java.sql.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DataUtilTest {

    @Test
    public void converterDateUtilParaDateSql(){
        Date dataSql = DataUtil.converterDateUtilParaDateSql(new java.util.Date());

        Date dataEsperada = new java.sql.Date(new java.util.Date().getTime());

        assertEquals(dataEsperada.toString(), dataSql.toString());
    }

    @Test
    public void retornarDataAtual(){
        assertThat(DataUtil.retornarDataAtual(), is(new java.util.Date()));
    }
}
