package br.com.storeapplication.util;

public final class RedirecionarUtil {

	public static String redirectPagina(String pagina) {

		final String REDIRECIONAR_PAGINA = "?faces-redirect=true";

		pagina = pagina + REDIRECIONAR_PAGINA;

		return pagina;
	}

}
