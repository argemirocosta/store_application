package br.com.storeapplication.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.net.URL;
import java.util.Iterator;

class CEPWebService {

	private String estado = "";
	private String cidade = "";
	private String bairro = "";
	private String logradouro = "";
	private Integer codIBGE = 0;

	@SuppressWarnings("rawtypes")
	CEPWebService(String cep) {
		try {
			URL url = new URL(
					"http://viacep.com.br/ws/" + cep.replaceAll("[^0-9]", "") + "/xml/");

			Document document = getDocumento(url);

			Element root = document.getRootElement();

			for (Iterator i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();

				if (element.getQualifiedName().equals("uf"))
					setEstado(element.getText());

				if (element.getQualifiedName().equals("localidade"))
					setCidade(element.getText());

				if (element.getQualifiedName().equals("bairro"))
					setBairro(element.getText());

				if (element.getQualifiedName().equals("logradouro"))
					setLogradouro(element.getText());

				if (element.getQualifiedName().equals("ibge"))
					setCodIBGE(Integer.parseInt(element.getText()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Document getDocumento(URL url) throws DocumentException, SAXException {
		SAXReader reader = new SAXReader();
		reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
		reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		return reader.read(url);
	}

	String getEstado() {
		return estado;
	}

	private void setEstado(String estado) {
		this.estado = estado;
	}

	String getCidade() {
		return cidade;
	}

	private void setCidade(String cidade) {
		this.cidade = cidade;
	}

	String getBairro() {
		return bairro;
	}

	private void setBairro(String bairro) {
		this.bairro = bairro;
	}

	String getLogradouro() {
		return logradouro;
	}

	private void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Integer getCodIBGE() {
		return codIBGE;
	}

	public void setCodIBGE(Integer codIBGE) {
		this.codIBGE = codIBGE;
	}
}