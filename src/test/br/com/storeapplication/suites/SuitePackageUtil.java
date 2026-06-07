package br.com.storeapplication.suites;

import br.com.storeapplication.util.CEPUtilTest;
import br.com.storeapplication.util.DataUtilTest;
import br.com.storeapplication.util.DocumentosUtilTest;
import br.com.storeapplication.util.IntegerUtilTest;
import br.com.storeapplication.util.RedirecionarUtilTest;
import br.com.storeapplication.util.StringUtilTest;
import br.com.storeapplication.util.VerificadorUtilTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CEPUtilTest.class,
        DataUtilTest.class,
        DocumentosUtilTest.class,
        IntegerUtilTest.class,
        RedirecionarUtilTest.class,
        StringUtilTest.class,
        VerificadorUtilTest.class
})
public class SuitePackageUtil {
}
