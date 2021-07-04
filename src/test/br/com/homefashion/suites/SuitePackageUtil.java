package br.com.storeapplication.suites;

import br.com.storeapplication.util.DataUtilTest;
import br.com.storeapplication.util.DocumentosUtilTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DataUtilTest.class,
        DocumentosUtilTest.class
})

public class SuitePackageUtil {

    @BeforeClass
    public static void iniciarSuiteDeTeste(){
        System.out.println("Iniciando classe ");
    }

    @AfterClass
    public static void finalizarSuiteDeTeste(){
        System.out.println("Finalizando classe");
    }

}
