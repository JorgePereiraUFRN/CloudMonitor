package br.ufrn.consiste.CloudMonitor.model;

public class Machine {

	private long id;
	private String ip;
	private int tomCatPort;
	private Thresholds thresholds;
	
	
	public Thresholds getThresholds() {
		return thresholds;
	}

	public void setThresholds(Thresholds thresholds) {
		this.thresholds = thresholds;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

}
