package br.ufrn.consiste.CloudMonitor;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class TestCloudMonitor {

	public static void main(String[] args) {
		String portNum, registryURL;
		try {
			
			int RMIPortNum = 2000;
			startRegistry(RMIPortNum);
			CloudMonitor exportedObj = new CloudMonitorImpl();
			registryURL = "rmi://localhost:" + RMIPortNum + "/CloudMonitor";
			Naming.rebind(registryURL, exportedObj);
			System.out.println("Callback Server ready.");
		} catch (Exception re) {
			re.printStackTrace();
			System.out.println("Exception: " + re);
		}

	}

	private static void startRegistry(int RMIPortNum) throws RemoteException {
		try {
			Registry registry = LocateRegistry.getRegistry(RMIPortNum);
			registry.list();
		} catch (RemoteException e) {
			// No valid registry at that port.
			Registry registry = LocateRegistry.createRegistry(RMIPortNum);
		}
	} // end startRegistry
}
