package br.com.storeapplication.validator;

import org.junit.jupiter.api.Test;

import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CPFValidatorTest {

    private CPFValidator validator = new CPFValidator();

    @Test
    public void testarValidarCpfFormatadoValidoNaoLancaExcecao(){
        validator.validate(null, null, "256.235.615-27");
    }

    @Test
    public void testarValidarValorNuloNaoLancaExcecao(){
        validator.validate(null, null, null);
    }

    @Test
    public void testarValidarCpfInvalidoLancaValidatorExceptionComSeveridadeErro(){
        try {
            validator.validate(null, null, "111.111.111-11");
            fail("Era esperado o lançamento de ValidatorException para um CPF inválido");
        } catch (ValidatorException ex) {
            assertEquals(FacesMessage.SEVERITY_ERROR, ex.getFacesMessage().getSeverity());
            assertEquals("CPF não válido!", ex.getFacesMessage().getSummary());
        }
    }

}
