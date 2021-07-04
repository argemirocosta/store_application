package br.com.storeapplication.util;

import br.com.storeapplication.model.Endereco;

public final class CEPUtil {

    public static Endereco buscarEnderecoPorCEP(String cep) {
        Endereco endereco = new Endereco();

        CEPWebService cepWebService = new CEPWebService(cep);

        if (cepWebService.getCodIBGE() != 0) {
            endereco.setLogradouro(cepWebService.getLogradouro());
            endereco.setEstado(cepWebService.getEstado());
            endereco.setCidade(cepWebService.getCidade());
            endereco.setBairro(cepWebService.getBairro());
            endereco.setCodIBGE(cepWebService.getCodIBGE());
        }

        return endereco;
    }

}
