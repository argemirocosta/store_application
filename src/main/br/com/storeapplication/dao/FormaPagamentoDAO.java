package br.com.storeapplication.dao;

import br.com.storeapplication.factory.ConnectionFactory;
import br.com.storeapplication.model.FormaPagamento;
import br.com.storeapplication.model.builder.FormaPagamentoBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static br.com.storeapplication.shared.queries.FormasPagamentoDAOQueries.SELECT_LISTAR_FORMAS_PAGAMENTO;

public class FormaPagamentoDAO {

	private Connection conexao = null;

	public List<FormaPagamento> listarFormasPagamento() {

		conexao = ConnectionFactory.getConnection();

		List<FormaPagamento> listaFormasPagamento = new ArrayList<>();

		try {
			PreparedStatement ps = conexao.prepareStatement(SELECT_LISTAR_FORMAS_PAGAMENTO);
			ResultSet rs = ps.executeQuery();

			listaFormasPagamento = mapearResultSetIniciarListaFormasPagamento(rs);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return listaFormasPagamento;
	}

	private ArrayList<FormaPagamento> mapearResultSetIniciarListaFormasPagamento(ResultSet rs) {

		ArrayList<FormaPagamento> listaFormasPagamento = new ArrayList<>();

		try {
			while (rs.next()) {
				FormaPagamentoBuilder formaPagamentoBuilder = new FormaPagamentoBuilder();
				listaFormasPagamento.add(formaPagamentoBuilder.mapear(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaFormasPagamento;
	}

}