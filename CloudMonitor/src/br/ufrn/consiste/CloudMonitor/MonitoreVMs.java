package br.ufrn.consiste.CloudMonitor;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.w3c.dom.ls.LSInput;

import br.ufrn.consiste.CloudMonitor.model.Machine;
import br.ufrn.consiste.CloudMonitor.model.Thresholds;
import br.ufrn.consiste.VmMonitor.resources.ResourcesUsage;

public class MonitoreVMs extends Thread {

	private volatile Map<Long, Client> clients;
	private volatile Map<Long, Map<Long, Machine>> machinesClient;

	private Executor executor = Executors.newCachedThreadPool();

	public MonitoreVMs(Map<Long, Client> clients,
			Map<Long, Map<Long, Machine>> machinesClient) {
		super();
		this.clients = clients;
		this.machinesClient = machinesClient;
	}

	public void run() {

		for (;;) {
			System.out.println("iniciando monitoramento");

			long initialTime = System.currentTimeMillis();

			Iterator<Long> clientsIterator = clients.keySet().iterator();

			while (clientsIterator.hasNext()) {
				long idclient = clientsIterator.next();

				final Client client = clients.get(idclient);

				if (client != null) {

					Iterator<Machine> machines = machinesClient.get(idclient)
							.values().iterator();

					while (machines.hasNext()) {

						final Machine m = machines.next();

						executor.execute(new Runnable() {

							@Override
							public void run() {
								try {
									checkThresholds(client, m);
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});

					}

				}
			}

			long finalTime = System.currentTimeMillis();

			long time = finalTime - initialTime;

			long sleeptime = (1000 * 30 * 1) - time;

			if (sleeptime > 0) {

				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}

	}

	private void checkThresholds(Client clientCallback, Machine machine)
			throws RemoteException {

		String uriVmMonitor = "http://" + machine.getIp() + ":"
				+ machine.getTomCatPort() + "/VmMonitor/Metrics";
		RetrieveMetrics retrieveMetrics = new RetrieveMetrics(uriVmMonitor);

		ResourcesUsage reuUsage = retrieveMetrics.getAverageResoucesUsage();
		Thresholds thresholds = machine.getThresholds();

		if (reuUsage != null) {

			if (reuUsage.getCpuUsage() < thresholds.getCpuMinThreshold()
					|| reuUsage.getCpuUsage() > thresholds.getCpuMaxThreshold()) {

				clientCallback.cpuThreshold(reuUsage.getCpuUsage());
			}

			if (reuUsage.getMemoryUsage() < thresholds.getMemoryMinThreshold()
					|| reuUsage.getMemoryUsage() > thresholds
							.getMemoryMaxThreshold()) {
				clientCallback.memoryThreshold(reuUsage.getMemoryUsage());
			}

			if (reuUsage.getStorageUsage() < thresholds
					.getStorageMinThreshold()
					|| reuUsage.getStorageUsage() > thresholds
							.getStorageMaxThreshold()) {

				clientCallback.storageThresold(reuUsage.getStorageUsage());
			}

			if (reuUsage.getTxBytes() < thresholds.getTxBytesMinThreshold()
					|| reuUsage.getTxBytes() > thresholds
							.getTxBytesMaxThreshold()) {

				clientCallback.txBytesThreshold(reuUsage.getRxBytes());
			}

			if (reuUsage.getRxBytes() < thresholds.getRxBytesMinThreshold()
					|| reuUsage.getRxBytes() > thresholds
							.getRxBytesMaxThreshold()) {

				clientCallback.rxBytesthreshold(reuUsage.getRxBytes());
			}
		}

	}

}
