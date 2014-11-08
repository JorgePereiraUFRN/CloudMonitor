package br.ufrn.consiste.model;

import java.util.ArrayList;
import java.util.List;

public class Client {

	private long id;
	private String rmiURL;
	List<Machine> machines = new ArrayList<>();

	public String getRmiURL() {
		return rmiURL;
	}

	public void setRmiURL(String rmiURL) {
		this.rmiURL = rmiURL;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Machine> getMachines() {
		return machines;
	}

	public void setMachines(List<Machine> machines) {
		this.machines = machines;
	}

	public Client() {
		// TODO Auto-generated constructor stub
	}
	
	public String toString(){
		return "id: "+id+" rmiURL: "+rmiURL;
		
	}

}
