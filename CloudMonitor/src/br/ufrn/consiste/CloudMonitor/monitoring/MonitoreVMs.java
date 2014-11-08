package br.ufrn.consiste.CloudMonitor.monitoring;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import br.ufrn.consiste.CloudMonitor.ClientCloudMonitor;
import br.ufrn.consiste.CloudMonitor.DAO.ClientDao;
import br.ufrn.consiste.CloudMonitor.DAO.ClientDaoInterface;
import br.ufrn.consiste.CloudMonitor.DAO.ClientDaoJdbc;
import br.ufrn.consiste.CloudMonitor.DAO.MachineDaoInterface;
import br.ufrn.consiste.CloudMonitor.DAO.MachineDaoJdbc;
import br.ufrn.consiste.CloudMonitor.DAO.ResourceUsageDao;
import br.ufrn.consiste.CloudMonitor.DAO.ResourceUsageDaoInterface;
import br.ufrn.consiste.CloudMonitor.DAO.ResourceUsageDaoJdbc;
import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;
import br.ufrn.consiste.CloudMonitor.Exceptions.RetrieveMetricsMachineException;
import br.ufrn.consiste.model.Client;
import br.ufrn.consiste.model.Machine;
import br.ufrn.consiste.model.ResourcesUsage;
import br.ufrn.consiste.model.Thresholds;

public class MonitoreVMs extends Thread {

	private Executor executor = Executors.newCachedThreadPool();

	private static ClientDaoInterface clientDao2 = new ClientDaoJdbc();
	private MachineDaoInterface machineDao = new MachineDaoJdbc();
	private static ResourceUsageDaoInterface resourceUsageDao2 = new ResourceUsageDaoJdbc();

	public MonitoreVMs() {
		super();

	}

	public void run() {

		for (;;) {
			System.out.println("iniciando monitoramento");

			try {

				long initialTime = System.currentTimeMillis();

				Iterator<Machine> machines = machineDao.findAll().iterator();

				while (machines.hasNext()) {

					final Machine m = machines.next();

					executor.execute(new Runnable() {

						@Override
						public void run() {
							try {
								ResourcesUsage reUsage = getAtualResourceUsage(m);
								if (reUsage != null) {
									resourceUsageDao2.save(reUsage);
									checkThresholds(reUsage, m);
								}

							} catch (RemoteException | MalformedURLException
									| NotBoundException | DAOException
									| RetrieveMetricsMachineException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});

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
			} catch (Exception e) {
				// TODO: logar a exceção
				e.printStackTrace();
			}

		}

	}

	private ResourcesUsage getAtualResourceUsage(Machine machine)
			throws RetrieveMetricsMachineException {

		ResourcesUsage resourcesUsage = null;

		String uriVmMonitor = "http://" + machine.getIp() + ":"
				+ machine.getTomCatPort() + "/VmMonitor/Metrics";

		RetrieveMetrics retrieveMetrics = new RetrieveMetrics(uriVmMonitor);

		ResourcesUsage[] reUsage = retrieveMetrics.getLastMetricsVM();

		if (reUsage != null) {
			resourcesUsage = retrieveMetrics.getAverageResoucesUsage(reUsage);
			if (resourcesUsage != null) {
				resourcesUsage.setDate(new Date());
				resourcesUsage.setMachineId(machine.getId());
			}
		}

		return resourcesUsage;
	}

	private void checkThresholds(ResourcesUsage reuUsage, Machine machine)
			throws RemoteException, MalformedURLException, NotBoundException,
			DAOException {

		Thresholds thresholds = machine.getThresholds();

		if (reuUsage != null) {

			if (reuUsage.getCpuUsage() < thresholds.getCpuMinThreshold()
					|| reuUsage.getCpuUsage() > thresholds.getCpuMaxThreshold()) {

				Client c = clientDao2.findById(machine.getClientId());

				getClientCloudMonitor(c).cpuThreshold(machine.getId(),
						reuUsage.getCpuUsage());
			}

			if (reuUsage.getMemoryUsage() < thresholds.getMemoryMinThreshold()
					|| reuUsage.getMemoryUsage() > thresholds
							.getMemoryMaxThreshold()) {
				Client c = clientDao2.findById(machine.getClientId());

				getClientCloudMonitor(c).memoryThreshold(machine.getId(),
						reuUsage.getMemoryUsage());
			}

			if (reuUsage.getStorageUsage() < thresholds
					.getStorageMinThreshold()
					|| reuUsage.getStorageUsage() > thresholds
							.getStorageMaxThreshold()) {
				Client c = clientDao2.findById(machine.getClientId());

				getClientCloudMonitor(c).storageThresold(machine.getId(),
						reuUsage.getStorageUsage());
			}

			if (reuUsage.getTxBytes() < thresholds.getTxBytesMinThreshold()
					|| reuUsage.getTxBytes() > thresholds
							.getTxBytesMaxThreshold()) {
				Client c = clientDao2.findById(machine.getClientId());

				getClientCloudMonitor(c).txBytesThreshold(machine.getId(),
						reuUsage.getRxBytes());
			}

			if (reuUsage.getRxBytes() < thresholds.getRxBytesMinThreshold()
					|| reuUsage.getRxBytes() > thresholds
							.getRxBytesMaxThreshold()) {
				Client c = clientDao2.findById(machine.getClientId());

				getClientCloudMonitor(c).rxBytesthreshold(machine.getId(),
						reuUsage.getRxBytes());
			}
		}

	}

	private ClientCloudMonitor getClientCloudMonitor(Client c)
			throws MalformedURLException, RemoteException, NotBoundException {

		ClientCloudMonitor client = (ClientCloudMonitor) Naming.lookup(c
				.getRmiURL());

		return client;
	}

}
