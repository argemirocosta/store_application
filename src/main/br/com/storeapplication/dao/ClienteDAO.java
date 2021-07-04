package br.com.storeapplication.dao;

import br.com.storeapplication.exception.ProjetoException;
import br.com.storeapplication.factory.ConnectionFactory;
import br.com.storeapplication.model.Cliente;
import br.com.storeapplication.model.Usuario;
import br.com.storeapplication.model.builder.ClienteBuilder;
import br.com.storeapplication.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static br.com.storeapplication.shared.queries.ClienteDAOQueries.*;
import static br.com.storeapplication.shared.Sessao.*;

public class ClienteDAO {

	private Connection conexao = null;

	private Usuario usuarioSessao = (Usuario) SessaoUtil.resgatarDaSessao(USUARIO_SESSAO);

	public List<Cliente> listarClientes() {

		conexao = ConnectionFactory.getConnection();

		List<Cliente> listaClientes = new ArrayList<>();

		try {
			PreparedStatement ps = conexao.prepareStatement(SELECT_LISTAR_CLIENTES);
			ps.setInt(1, usuarioSessao.getId());
			ResultSet rs = ps.executeQuery();

			listaClientes = mapearResultSetIniciarListaClientes(rs);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return listaClientes;
	}

	public List<Cliente> buscarClientePorNome(String campoBusca) {

		conexao = ConnectionFactory.getConnection();

		List<Cliente> listaClientes = new ArrayList<>();

		try {
			PreparedStatement ps = conexao.prepareStatement(SELECT_BUSCAR_CLIENTE_POR_NOME_OU_TELEFONE);
			ps.setString(1, "%" + campoBusca + "%");
			ps.setString(2, "%" + campoBusca + "%");
			ps.setInt(3, usuarioSessao.getId());
			ResultSet rs = ps.executeQuery();

			listaClientes = mapearResultSetIniciarListaClientes(rs);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return listaClientes;
	}

	public List<Cliente> buscarClientePorId(int idCliente) {

		conexao = ConnectionFactory.getConnection();

		List<Cliente> listaClientes = new ArrayList<>();

		try {
			PreparedStatement ps = conexao.prepareStatement(SELECT_BUSCAR_CLIENTE_POR_ID);
			ps.setInt(1, idCliente);
			ps.setInt(2, usuarioSessao.getId());
			ResultSet rs = ps.executeQuery();

			listaClientes = mapearResultSetIniciarListaClientes(rs);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return listaClientes;
	}

	private ArrayList<Cliente> mapearResultSetIniciarListaClientes(ResultSet rs) {

		ArrayList<Cliente> listaClientes = new ArrayList<>();

		try {
			while (rs.next()) {
				ClienteBuilder clienteBuilder = new ClienteBuilder();
				listaClientes.add(clienteBuilder.mapear(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaClientes;
	}

	public void inserirCliente(Cliente cliente) throws ProjetoException {

		conexao = ConnectionFactory.getConnection();

		try {
			PreparedStatement ps = conexao.prepareStatement(INSERIR_CLIENTE);
			ps.setString(1, cliente.getNome().toUpperCase());

			if (VerificadorUtil.verificarSeObjetoNulo(cliente.getTelefone1())) {
				ps.setNull(2, Types.NULL);
			} else {
				ps.setString(2, cliente.getTelefone1());
			}

			if (VerificadorUtil.verificarSeObjetoNulo(cliente.getTelefone2())) {
				ps.setNull(3, Types.NULL);
			} else {
				ps.setInt(3, cliente.getTelefone2());
			}

			ps.setInt(4, usuarioSessao.getId());

			if(VerificadorUtil.verificarSeObjetoNulo(cliente.getDataNascimento())){
				ps.setNull(5, Types.NULL);
			}
			else {
				ps.setDate(5, DataUtil.converterDateUtilParaDateSql(cliente.getDataNascimento()));
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getCpf())){
				ps.setNull(6, Types.CHAR);
			}
			else {
				ps.setString(6, StringUtil.retirarCaracteresEspeciais(cliente.getCpf()));
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getRg())){
				ps.setNull(7, Types.CHAR);
			}
			else {
				ps.setString(7, cliente.getRg());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getCep())){
				ps.setNull(8, Types.CHAR);
			}
			else {
				ps.setString(8, StringUtil.retirarCaracteresEspeciais(cliente.getEndereco().getCep()));
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getEstado())){
				ps.setNull(9, Types.CHAR);
			}
			else {
				ps.setString(9, cliente.getEndereco().getEstado());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getCidade())){
				ps.setNull(10, Types.CHAR);
			}
			else {
				ps.setString(10, cliente.getEndereco().getCidade());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getBairro())){
				ps.setNull(11, Types.CHAR);
			}
			else {
				ps.setString(11, cliente.getEndereco().getBairro());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getLogradouro())){
				ps.setNull(12, Types.CHAR);
			}
			else {
				ps.setString(12, cliente.getEndereco().getLogradouro());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getNumero())){
				ps.setNull(13, Types.NULL);
			}
			else {
				ps.setInt(13, cliente.getEndereco().getNumero());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getCodIBGE())){
				ps.setNull(14, Types.NULL);
			}
			else {
				ps.setInt(14, cliente.getEndereco().getCodIBGE());
			}

			ResultSet rs = ps.executeQuery();
			Integer idCliente = null;
			if (rs.next()) {
				idCliente = rs.getInt("id");
			}

			SessaoUtil.adicionarNaSessao(idCliente, "clienteCriado");

			ps.execute();

			conexao.commit();

		} catch (SQLException ex) {
			throw  new ProjetoException(ex);
		} finally {
			try {
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void alterarCliente(Cliente cliente) throws ProjetoException {

		conexao = ConnectionFactory.getConnection();

		try {
			PreparedStatement ps = conexao.prepareStatement(ALTERAR_CLIENTE);
			ps.setString(1, cliente.getNome().toUpperCase());

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getTelefone1())){
				ps.setNull(2, Types.NULL);
			}
			else {
				ps.setString(2, cliente.getTelefone1());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getTelefone2())){
				ps.setNull(3, Types.NULL);
			}
			else {
				ps.setInt(3, cliente.getTelefone2());
			}

			if(VerificadorUtil.verificarSeObjetoNulo(cliente.getDataNascimento())){
				ps.setNull(4, Types.NULL);
			}
			else {
				ps.setDate(4, DataUtil.converterDateUtilParaDateSql(cliente.getDataNascimento()));
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getCpf())){
				ps.setNull(5, Types.CHAR);
			}
			else {
				ps.setString(5, StringUtil.retirarCaracteresEspeciais(cliente.getCpf()));
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getRg())){
				ps.setNull(6, Types.CHAR);
			}
			else {
				ps.setString(6, cliente.getRg());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getCep())){
				ps.setNull(7, Types.CHAR);
			}
			else {
				ps.setString(7, StringUtil.retirarCaracteresEspeciais(cliente.getEndereco().getCep()));
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getEstado())){
				ps.setNull(8, Types.CHAR);
			}
			else {
				ps.setString(8, cliente.getEndereco().getEstado());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getCidade())){
				ps.setNull(9, Types.CHAR);
			}
			else {
				ps.setString(9, cliente.getEndereco().getCidade());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getBairro())){
				ps.setNull(10, Types.CHAR);
			}
			else {
				ps.setString(10, cliente.getEndereco().getBairro());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getLogradouro())){
				ps.setNull(11, Types.CHAR);
			}
			else {
				ps.setString(11, cliente.getEndereco().getLogradouro());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getNumero())){
				ps.setNull(12, Types.NULL);
			}
			else {
				ps.setInt(12, cliente.getEndereco().getNumero());
			}

			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cliente.getEndereco().getCodIBGE())){
				ps.setNull(13, Types.NULL);
			}
			else {
				ps.setInt(13, cliente.getEndereco().getCodIBGE());
			}

			ps.setInt(14, cliente.getId());

			ps.executeUpdate();

			conexao.commit();

		} catch (SQLException ex) {
			throw new ProjetoException(ex);
		} finally {
			try {
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void deletarCliente(Cliente cliente) throws ProjetoException {

		conexao = ConnectionFactory.getConnection();

		try {
			PreparedStatement ps = conexao.prepareStatement(DELETAR_CLIENTE);

			ps.setInt(1, cliente.getId());

			ps.execute();

			conexao.commit();

		} catch (SQLException ex) {
			throw new ProjetoException(ex);
		} finally {
			try {
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

}