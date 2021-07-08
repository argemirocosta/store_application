package br.com.storeapplication.factory;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private static final String DRIVER_CLASS = "org.postgresql.Driver";

	public static Connection getConnection() {

		Connection conexao = null;

		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		try {
			Conexoes conexoes = ConexaoBuilder.carregarDadosConexao();
			conexao = DriverManager.getConnection(conexoes.getUrlBanco(), conexoes.getUsuario(), conexoes.getSenha());
			conexao.setAutoCommit(false);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return conexao;
	}
}