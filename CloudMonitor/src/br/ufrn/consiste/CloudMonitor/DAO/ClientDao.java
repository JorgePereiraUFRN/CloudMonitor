package br.ufrn.consiste.CloudMonitor.DAO;

import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;
import br.ufrn.consiste.model.Client;

public class ClientDao implements ClientDaoInterface {

	private ObjectContainer db;
	
	private static final ClientDao CLIENT_DAO = new ClientDao();

	private ClientDao() {
		
	}

	
	public static ClientDao getInstance(){
		return CLIENT_DAO;
	}
	
	private synchronized void openDB() {
		db =  Db4o.openFile("ClienteDatabase.db4o");
				
	}

	
	@Override
	public synchronized Client findById(Long id) throws DAOException {

		try {
			openDB();
			
			Client c = new Client();
			c.setId(id);

			ObjectSet<Client> result = db.queryByExample(c);

			if (result.hasNext()) {
				c = result.next();
				System.out.println("Client recuperado " + c);

				return c;

			} else {
				return null;
			}
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		} finally {
			db.close();
		}

	}

	@Override
	public synchronized List<Client> findAll() throws DAOException {

		try {
			
			openDB();
			
			ObjectSet<Client> result = db.queryByExample(Client.class);
			List<Client> clients = new ArrayList<Client>();

			while (result.hasNext()) {

				Client c = result.next();
				clients.add(c);
				System.out.println("Client " + c);
			}

			return clients;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		} finally {
			db.close();
		}
	}


	
	@Override
	public synchronized Client save(Client entity) throws DAOException {

		try {
			openDB();
			
			entity.setId(findAll().size());
			db.store(entity);
			System.out.println("Client salvo " + entity);

			return entity;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		} finally {
			db.close();
		}
	}

	@Override
	public synchronized  Client update(Client entity) throws DAOException {
		try {
			
			openDB();
			
			db.store(entity);
			System.out.println("Client atualizado " + entity);

			return entity;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		} finally {
			db.close();
		}
	}

	@Override
	public synchronized void delete(Client entity) throws DAOException {
		try {
			openDB();
			
			db.delete(entity);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		} finally {
			db.close();
		}

	}

}
