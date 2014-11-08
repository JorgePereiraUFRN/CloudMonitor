package br.ufrn.consiste.CloudMonitor.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;
import br.ufrn.consiste.model.Client;

public class ClientDaoJdbc implements ClientDaoInterface {

	private PreparedStatement pstm;
	private ResultSet rs;
	private final acessBD bd = new acessBD();

	public ClientDaoJdbc() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Client findById(Long id) throws DAOException {
		String sql = "Select * from client where id = ?";

		try {
			pstm = bd.conect().prepareStatement(sql);

			pstm.setLong(1, id);

			rs = pstm.executeQuery();

			Client c = null;

			if (rs.next()) {
				c = new Client();

				c.setId(rs.getLong("id"));
				c.setRmiURL(rs.getString("rmiURL"));
			}

			bd.disconect();

			return c;

		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}

	@Override
	public List<Client> findAll() throws DAOException {
		String sql = "Select * from client";

		try {
			pstm = bd.conect().prepareStatement(sql);

			List<Client> clients = new ArrayList<Client>();
			rs = pstm.executeQuery();

			Client c = null;

			while (rs.next()) {
				c = new Client();

				c.setId(rs.getLong("id"));
				c.setRmiURL(rs.getString("rmiURL"));
				clients.add(c);
			}

			bd.disconect();

			return clients;

		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}

	@Override
	public synchronized Client save(Client entity) throws DAOException {

		String sql = "Insert into client (rmiURL) values (?)";
		try {
			pstm = bd.conect().prepareStatement(sql);

			pstm.setString(1, entity.getRmiURL());
			pstm.execute();
			bd.disconect();

			pstm = bd.conect()
					.prepareStatement("select MAX(id) id from client");

			rs = pstm.executeQuery();

			if (rs.next()) {
				entity.setId(rs.getLong("id"));
			}

			bd.disconect();

			return entity;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}
	}

	@Override
	public Client update(Client entity) throws DAOException {
		String sql = "upadte client set rmiURL = ? where id = ?";
		try {
			pstm = bd.conect().prepareStatement(sql);
			pstm.setLong(1, entity.getId());

			pstm.setString(1, entity.getRmiURL());
			pstm.execute();
			bd.disconect();

			return entity;

		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}

	}

	@Override
	public void delete(Client entity) throws DAOException {
		String sql = "Delete from client where id = ?";
		try {
			pstm = (PreparedStatement) bd.conect().prepareStatement(sql);
			pstm.setLong(1, entity.getId());
			pstm.execute();
			bd.disconect();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}

	}

}
