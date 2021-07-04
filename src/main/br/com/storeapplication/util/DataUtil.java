package br.com.storeapplication.util;

import java.util.Date;

public final class DataUtil {

    public static java.sql.Date converterDateUtilParaDateSql(Date dataUtil) {

        return new java.sql.Date(dataUtil.getTime());
    }

    public static Date retornarDataAtual(){
        return new Date(System.currentTimeMillis());
    }

}
