package br.ufrn.consiste.CloudMonitor;

import br.ufrn.consiste.CloudMonitor.Exceptions.RetrieveMetricsMachineException;
import br.ufrn.consiste.CloudMonitor.monitoring.RetrieveMetrics;
import br.ufrn.consiste.model.ResourcesUsage;

public class TestRetrieveMetrics {

	public static void main(String args[]) throws RetrieveMetricsMachineException {

		RetrieveMetrics rmeMetrics = new RetrieveMetrics(
				"http://localhost:8080/VmMonitor/Metrics");

		ResourcesUsage reUsage[] = rmeMetrics.getLastMetricsVM();

		for (ResourcesUsage r : reUsage) {
			System.out.println("cpuUsage: " + r.getCpuUsage()
					+ " MemoryUsage: " + r.getMemoryUsage()
					+ "\n storageUsage: " + r.getStorageUsage()
					+ " Send bytes: " + r.getTxBytes() + "\n Recieve bytes: "
					+ r.getRxBytes());
		}
	}

}
