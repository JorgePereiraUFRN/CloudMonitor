package br.ufrn.consiste.CloudMonitor.monitoring;

import java.rmi.RemoteException;
import java.util.Date;

import br.ufrn.consiste.CloudMonitor.Exceptions.RetrieveMetricsMachineException;
import br.ufrn.consiste.model.ResourcesUsage;
import br.ufrn.consiste.model.Thresholds;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class RetrieveMetrics {

	private Client client = Client.create();
	private String uriVmMonitor;

	public RetrieveMetrics(String uriVmMonitor) {
		this.uriVmMonitor = uriVmMonitor;
	}

	public ResourcesUsage getAverageResoucesUsage(ResourcesUsage[] reUsages) {

		ResourcesUsage reUsage = null;
		// ResourcesUsage[] reUsages = getLastMetricsVM();

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

	public ResourcesUsage[] getLastMetricsVM() throws RetrieveMetricsMachineException{

		try {

			ResourcesUsage reUsage[];

			WebResource resource = client.resource(uriVmMonitor
					+ "/LastMetrics");

			reUsage = resource.type("application/json").get(
					ResourcesUsage[].class);

			return reUsage;
		} catch (Exception e) {
			throw new RetrieveMetricsMachineException("Erro ao recuperar metricas da máquina\n"+e.getMessage());
		}
	}

}
