package br.com.storeapplication.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentosUtilTest {

    static Stream<Arguments> parametros() {
        String cpf1Correto = "25623561527";
        String cpf2Correto = "41329663217";
        String cpf1Errado = "11111111111";
        String cpf2Errado = "12345678912";
        String cpfTamanhoInvalido = "123456789";
        String cpfComLetras = "2562356152A";
        return Stream.of(
                Arguments.of(cpf1Errado, "Número Iguais", false),
                Arguments.of(cpf2Errado, "CPF Errado", false),
                Arguments.of(cpf1Correto, "CPF Correto", true),
                Arguments.of(cpf2Correto, "CPF Correto", true),
                Arguments.of(cpfTamanhoInvalido, "Tamanho Inválido", false),
                Arguments.of(cpfComLetras, "Caracteres Não Numéricos", false)
        );
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("parametros")
    public void testarSeCpfEhValido(String cpf, String cenario, boolean esperado) {
        assertEquals(esperado, DocumentosUtil.validaCPF(cpf));
    }
}
