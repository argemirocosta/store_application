package br.com.storeapplication.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RedirecionarUtilTest {

    @Test
    public void testarRedirectPaginaComNomeDePagina(){
        assertThat (RedirecionarUtil.redirectPagina("principal"), is("principal?faces-redirect=true"));
    }

    @Test
    public void testarRedirectPaginaComStringVazia(){
        assertThat (RedirecionarUtil.redirectPagina(""), is("?faces-redirect=true"));
    }

    @Test
    public void testarRedirectPaginaComValorNulo(){
        assertThat (RedirecionarUtil.redirectPagina(null), is("null?faces-redirect=true"));
    }

}
