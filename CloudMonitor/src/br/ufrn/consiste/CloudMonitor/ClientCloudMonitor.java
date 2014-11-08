package br.ufrn.consiste.CloudMonitor;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCloudMonitor extends Remote{
	
	void cpuThreshold(long vmId, double cpuUsage) throws RemoteException;
	
	void memoryThreshold(long vmId, double memoryUsage) throws RemoteException;
	
	void storageThresold(long vmId, double storageUsage) throws RemoteException;
	
	void txBytesThreshold(long vmId, long txBytes) throws RemoteException;
	
	void rxBytesthreshold(long vmId, long rxbytes) throws RemoteException;
	
	

}
