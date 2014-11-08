package br.ufrn.consiste.model;

import java.io.Serializable;


public class Thresholds implements Serializable {
	

	private double cpuMinThreshold;
	private double storageMinThreshold;
	private long txBytesMinThreshold;
	private long rxBytesMinThreshold;
	private double memoryMinThreshold;
	
	private double cpuMaxThreshold;
	private double memoryMaxThreshold;
	private double storageMaxThreshold;
	private long txBytesMaxThreshold;
	private long rxBytesMaxThreshold;
	


	public double getCpuMinThreshold() {
		return cpuMinThreshold;
	}
	public void setCpuMinThreshold(double cpuMinThreshold) {
		this.cpuMinThreshold = cpuMinThreshold;
	}
	public double getMemoryMinThreshold() {
		return memoryMinThreshold;
	}
	public void setMemoryMinThreshold(double memoryMinThreshold) {
		this.memoryMinThreshold = memoryMinThreshold;
	}
	public double getStorageMinThreshold() {
		return storageMinThreshold;
	}
	public void setStorageMinThreshold(double storageMinThreshold) {
		this.storageMinThreshold = storageMinThreshold;
	}
	public long getTxBytesMinThreshold() {
		return txBytesMinThreshold;
	}
	public void setTxBytesMinThreshold(long txBytesMinThreshold) {
		this.txBytesMinThreshold = txBytesMinThreshold;
	}
	public long getRxBytesMinThreshold() {
		return rxBytesMinThreshold;
	}
	public void setRxBytesMinThreshold(long rxBytesMinThreshold) {
		this.rxBytesMinThreshold = rxBytesMinThreshold;
	}
	public double getCpuMaxThreshold() {
		return cpuMaxThreshold;
	}
	public void setCpuMaxThreshold(double cpuMaxThreshold) {
		this.cpuMaxThreshold = cpuMaxThreshold;
	}
	public double getMemoryMaxThreshold() {
		return memoryMaxThreshold;
	}
	public void setMemoryMaxThreshold(double memoryMaxThreshold) {
		this.memoryMaxThreshold = memoryMaxThreshold;
	}
	public double getStorageMaxThreshold() {
		return storageMaxThreshold;
	}
	public void setStorageMaxThreshold(double storageMaxThreshold) {
		this.storageMaxThreshold = storageMaxThreshold;
	}
	public long getTxBytesMaxThreshold() {
		return txBytesMaxThreshold;
	}
	public void setTxBytesMaxThreshold(long txBytesMaxThreshold) {
		this.txBytesMaxThreshold = txBytesMaxThreshold;
	}
	public long getRxBytesMaxThreshold() {
		return rxBytesMaxThreshold;
	}
	public void setRxBytesMaxThreshold(long rxBytesMaxThreshold) {
		this.rxBytesMaxThreshold = rxBytesMaxThreshold;
	}

	
	
}
