package br.com.storeapplication.util;

import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataUtilTest {

    @Test
    public void converterDateUtilParaDateSql(){
        java.util.Date dataUtilOriginal = new java.util.Date(1700000000000L);

        Date dataSql = DataUtil.converterDateUtilParaDateSql(dataUtilOriginal);

        assertEquals(dataUtilOriginal.getTime(), dataSql.getTime());
    }

    @Test
    public void retornarDataAtual(){
        long antes = System.currentTimeMillis();

        java.util.Date dataAtual = DataUtil.retornarDataAtual();

        long depois = System.currentTimeMillis();

        assertTrue(dataAtual.getTime() >= antes && dataAtual.getTime() <= depois);
    }
}
