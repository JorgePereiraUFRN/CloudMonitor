package br.ufrn.consiste.model;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Database {

	final static String DB4OFILENAME = System.getProperty("user.home")
			+ "/dados.db4o";
	private ObjectContainer db;

	public Database() {
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
				DB4OFILENAME);
	}

	public Client getClientById(long id) {
		Client c = new Client();
		c.setId(id);

		ObjectSet result = db.queryByExample(c);

		if (result.hasNext()) {
			c = (Client) result.next();
			System.out.println("Client recuperado " + c);
		}

		return c;

	}

	public long saveClient(Client client) {

		db.store(client);
		System.out.println("Client salvo " + client);
		return 0;
	}

	public List<Client> getAll() {
		ObjectSet<Client> result = db.queryByExample(Client.class);
		List<Client> clients = new ArrayList<Client>();

		while (result.hasNext()) {

			Client c = result.next();
			clients.add(c);
			System.out.println("Client " + c);
		}

		return clients;
	}

	private ResourcesUsage getAverageUsaugeMachine(long machineId) {

		ResourcesUsage r = null;

		return r;
	}
	
	public static void listResult(List<?> result){
    	System.out.println(result.size());
        for (Object o : result) {
            System.out.println(o);
        }
    }

	public static void main(String args[]) {

		Database db = new Database();

		Client c = new Client();
		c.setId(1);
		c.setRmiURL("rmi:localhost/c1");
		
		//db.getAll();
		
		db.saveClient(c);
		
		Machine m1 = new Machine();
		m1.setId(1L);
		m1.setIp("localhost");
		
		
		c.getMachines().add(m1);
		
		db.saveClient(c);
		
		listResult(db.getClientById(c.getId()).getMachines());
		
		
		Machine m2 = new Machine();
		m2.setId(2L);
		m2.setIp("127.0.0.1");
		
		
		c.getMachines().add(m2);
		
		db.saveClient(c);
		
		listResult(db.getClientById(c.getId()).getMachines());
		
		

		/*db.saveClient(c);
		
		db.getAll();
		
		c.setId(2);
		db.saveClient(c);
		
		db.getAll();*/

		//db.getClientById(c.getId());

	}
	
	

}
