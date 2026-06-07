package br.com.storeapplication.util;

import br.com.storeapplication.model.Endereco;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class CEPUtilTest {

    @Test
    public void buscarEnderecoPorCEPValidoPreencheEndereco() {
        Endereco endereco = CEPUtil.buscarEnderecoPorCEP("01001000");

        assertThat(endereco.getEstado(), is("SP"));
        assertThat(endereco.getCidade(), is("São Paulo"));
        assertThat(endereco.getBairro(), is("Sé"));
        assertThat(endereco.getLogradouro(), is("Praça da Sé"));
        assertThat(endereco.getCodIBGE(), is(3550308));
    }

    @Test
    public void buscarEnderecoPorCEPInexistenteRetornaEnderecoVazio() {
        Endereco endereco = CEPUtil.buscarEnderecoPorCEP("99999999");

        assertThat(endereco.getCodIBGE(), is(nullValue()));
        assertThat(endereco.getLogradouro(), is(nullValue()));
    }
}
