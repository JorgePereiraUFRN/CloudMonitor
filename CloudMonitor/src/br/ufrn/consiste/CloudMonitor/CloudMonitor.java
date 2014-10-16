package br.ufrn.consiste.CloudMonitor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import br.ufrn.consiste.CloudMonitor.model.Thresholds;
import br.ufrn.consiste.VmMonitor.resources.ResourcesUsage;

public interface CloudMonitor extends Remote {

	long register(Client callbakClient) throws RemoteException;

	void unregister(long clientId) throws RemoteException;

	long monitoreVM(long clientId, String ipVm, int tomcatPort,
			String tomcatUser, String tomcatPassword, Thresholds thresholds)
			throws RemoteException;

	void monitoringVMCancel(long clientId, long vmId) throws RemoteException;

	Map<Long, ResourcesUsage> getMestricsVMs(long clientId, Long[] VMsId)
			throws RemoteException;

}
