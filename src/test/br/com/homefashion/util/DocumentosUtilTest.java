package br.com.storeapplication.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DocumentosUtilTest {

    @Parameterized.Parameter
    public String cpf;

    @Parameterized.Parameter(value = 1)
    public String cenario;

    @Parameterized.Parameters(name = "{1}")
    public static Collection parametros() {
        String cpf1Correto = "25623561527";
        String cpf2Correto = "41329663217";
        String cpf1Errado = "11111111111";
        String cpf2Errado = "12345678912";
        return Arrays.asList(new Object[][] {
                {cpf1Errado, "Número Iguais"},
                {cpf2Errado, "CPF Errado"},
                {cpf1Correto, "CPF Correto"},
                {cpf2Correto, "CPF Correto"}
        });
    }

    @Test
    public void testarSeCpfEhValido() {
        if(cenario.contains("Correto"))
            assertThat(DocumentosUtil.validaCPF(cpf), is(true));
        else
            assertThat(DocumentosUtil.validaCPF(cpf), is(false));
    }
}
