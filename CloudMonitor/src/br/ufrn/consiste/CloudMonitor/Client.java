package br.ufrn.consiste.CloudMonitor;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote{
	
	void cpuThreshold(double cpuUsage) throws RemoteException;
	
	void memoryThreshold(double memoryUsage) throws RemoteException;
	
	void storageThresold(double storageUsage) throws RemoteException;
	
	void txBytesThreshold(long txBytes) throws RemoteException;
	
	void rxBytesthreshold(long rxbytes) throws RemoteException;
	
	

}
