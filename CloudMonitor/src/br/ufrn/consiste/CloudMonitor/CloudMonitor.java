package br.ufrn.consiste.CloudMonitor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import br.ufrn.consiste.model.ResourcesUsage;
import br.ufrn.consiste.model.Thresholds;

public interface CloudMonitor extends Remote {

	long register(String rmiURL) throws RemoteException;

	void unregister(Long clientId) throws RemoteException;

	long monitoreVM(Long clientId, String ipVm, int tomcatPort,
			String tomcatUser, String tomcatPassword, Thresholds thresholds)
			throws RemoteException;

	void monitoringVMCancel(Long clientId, Long vmId) throws RemoteException;
	
	void updatThresholds(Long clientId, Long vmId, Thresholds thresholds) throws RemoteException;

	Map<Long, ResourcesUsage> getMestricsVMs(Long clientId, Long[] VMsId)
			throws RemoteException;

}
