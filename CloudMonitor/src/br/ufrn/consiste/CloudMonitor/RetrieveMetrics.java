package br.ufrn.consiste.CloudMonitor;

import java.rmi.RemoteException;

import br.ufrn.consiste.CloudMonitor.model.Thresholds;
import br.ufrn.consiste.VmMonitor.resources.ResourcesUsage;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class RetrieveMetrics {

	private Client client = Client.create();
	private String uriVmMonitor;

	public RetrieveMetrics(String uriVmMonitor) {
		this.uriVmMonitor = uriVmMonitor;
	}

	

	public ResourcesUsage getAverageResoucesUsage() {

		ResourcesUsage reUsage = null;
		ResourcesUsage[] reUsages = getLastMetricsVM();

		System.out.println("monitorando alguma vm");
		if (reUsages != null && reUsages.length > 0) {
			reUsage = new ResourcesUsage();
			
			double cpuUsage = 0, memoryUsage = 0, storageUsage = 0;
			long txBytes = 0, rxBytes = 0;

			for (ResourcesUsage r : reUsages) {

				cpuUsage += r.getCpuUsage();
				memoryUsage += r.getMemoryUsage();
				storageUsage += r.getStorageUsage();
				rxBytes += r.getRxBytes();
				txBytes += r.getTxBytes();
			}

			
			reUsage.setCpuUsage(cpuUsage / reUsages.length);
			reUsage.setMemoryUsage(memoryUsage / reUsages.length);
			reUsage.setRxBytes(rxBytes / reUsages.length);
			reUsage.setStorageUsage(storageUsage / reUsages.length);
			reUsage.setTxBytes(txBytes / reUsages.length);
		}

		return reUsage;

	}

	public ResourcesUsage[] getLastMetricsVM() {

		ResourcesUsage reUsage[];

		WebResource resource = client.resource(uriVmMonitor + "/LastMetrics");

		reUsage = resource.type("application/json").get(ResourcesUsage[].class);
		/*
		 * for (ResourcesUsage r : reUsage) { System.out.println("cpuUsage: " +
		 * r.getCpuUsage() + " MemoryUsage: " + r.getMemoryUsage() +
		 * "\n storageUsage: " + r.getStorageUsage() + " Send bytes: " +
		 * r.getTxBytes() + "\n Recieve bytes: " + r.getRxBytes()); }
		 */
		return reUsage;
	}

}
