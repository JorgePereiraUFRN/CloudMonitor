package br.ufrn.consiste.model;

public class Machine {

	private Long id;
	private String ip;
	private int tomCatPort;
	private Thresholds thresholds;
	private String tomcatUser;
	private long clientId;
	
	

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public String getTomcatUser() {
		return tomcatUser;
	}

	public void setTomcatUser(String tomcatUser) {
		this.tomcatUser = tomcatUser;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Thresholds getThresholds() {
		return thresholds;
	}

	public void setThresholds(Thresholds thresholds) {
		this.thresholds = thresholds;
	}


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getTomCatPort() {
		return tomCatPort;
	}

	public void setTomCatPort(int tomCatPort) {
		this.tomCatPort = tomCatPort;
	}
	
	public String toString(){
		return "id :"+id+" tomcatPort: "+tomCatPort+" tomCatUser: "+tomcatUser+
				" ip: "+ip+" client : "+clientId;
		
	}

}
