package br.com.storeapplication.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Venda implements Serializable {

	private Integer id;
	private Double valor;
	private Date data;
	private Integer qtd;
	private Cliente cliente;
	private Double totalPago;
	private Double emAberto;
	private String situacao;

	public Venda() {
		cliente = new Cliente();
		data = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getQtd() {
		return qtd;
	}

	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Double getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(Double totalPago) {
		this.totalPago = totalPago;
	}

	public Double getEmAberto() {
		return emAberto;
	}

	public void setEmAberto(Double emAberto) {
		this.emAberto = emAberto;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Venda venda = (Venda) o;
		return Objects.equals(id, venda.id) &&
				Objects.equals(valor, venda.valor) &&
				Objects.equals(data, venda.data) &&
				Objects.equals(qtd, venda.qtd) &&
				Objects.equals(cliente, venda.cliente) &&
				Objects.equals(totalPago, venda.totalPago) &&
				Objects.equals(emAberto, venda.emAberto) &&
				Objects.equals(situacao, venda.situacao);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, valor, data, qtd, cliente, totalPago, emAberto, situacao);
	}

	@Override
	public String toString() {
		return "Venda{" +
				"id=" + id +
				", valor=" + valor +
				", data=" + data +
				", qtd=" + qtd +
				", cliente=" + cliente +
				", totalPago=" + totalPago +
				", emAberto=" + emAberto +
				", situacao='" + situacao + '\'' +
				'}';
	}
}