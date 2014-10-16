package br.ufrn.consiste.CloudMonitor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.catalina.ant.DeployTask;

import br.ufrn.consiste.CloudMonitor.model.Machine;
import br.ufrn.consiste.CloudMonitor.model.Thresholds;
import br.ufrn.consiste.VmMonitor.resources.ResourcesUsage;

public class CloudMonitorImpl extends UnicastRemoteObject implements
		CloudMonitor {

	protected CloudMonitorImpl() throws RemoteException {
		super();
		new MonitoreVMs(clients, machinesClient).start();
	}

	// map<idclient, client>
	private static final Map<Long, Client> clients = Collections
			.synchronizedMap(new HashMap<Long, Client>());
	// map<idCliet, map<machineId, machine>>
	private static final Map<Long, Map<Long, Machine>> machinesClient = Collections
			.synchronizedMap(new HashMap<Long, Map<Long, Machine>>());

	@Override
	public long register(Client callbakClient) throws RemoteException {

		long id = clients.size();

		clients.put(id, callbakClient);
		machinesClient.put(id, new HashMap<Long, Machine>());

		return id;
	}

	@Override
	public void unregister(long clientId) throws RemoteException {

		clients.remove(clientId);
		machinesClient.remove(clientId);

	}

	@Override
	public long monitoreVM(long clientID, String ipVm, int tomcatPort,
			String tomcatUser, String tomcatPassword, Thresholds thresholds)
			throws RemoteException {

		deploymentVmMonitor(ipVm, tomcatPort, tomcatUser, tomcatPassword);

		/*
		 * //gambiarra rever isso no projeto VmMonitor new
		 * RetrieveMetrics("http://" + ipVm + ":" + tomcatPort + "/VmMonitor")
		 * .getLastMetricsVM();
		 */

		Map<Long, Machine> machines = machinesClient.get(clientID);

		if (machines != null) {
			Machine m = new Machine();

			m.setIp(ipVm);
			m.setTomCatPort(tomcatPort);
			m.setThresholds(thresholds);
			m.setId(machines.size());

			machines.put(m.getId(), m);

			System.out.println("nova vm cadastrada");
			return m.getId();
		}
		return 0;
	}

	@Override
	public void monitoringVMCancel(long clientId, long vmId)
			throws RemoteException {

		Map<Long, Machine> machines = machinesClient.get(clientId);

		if (machines != null) {
			machines.remove(vmId);
		}

	}

	private void deploymentVmMonitor(String ip, int port, String tomcatUser,
			String tomcatPassword) {

		DeployTask task = new DeployTask();
		task.setUrl("http://" + ip + ":" + port + "/manager/text");
		task.setUsername(tomcatUser);
		task.setPassword(tomcatPassword);
		task.setPath("/VmMonitor");
		task.setWar(new File("VmMonitor.war").getAbsolutePath());

		task.execute();

	}

	@Override
	public Map<Long, ResourcesUsage> getMestricsVMs(long clientId, Long[] VMsId)
			throws RemoteException {

		Map<Long, ResourcesUsage> metrics = new HashMap<Long, ResourcesUsage>();

		ExecutorService executor = Executors.newCachedThreadPool();

		List<Future<ResourcesUsage>> futureList = new ArrayList<Future<ResourcesUsage>>();

		Map<Long, Machine> machines = machinesClient.get(clientId);

		Set<Long> machinesId = machines.keySet();

		for (long id : machinesId) {

			final Machine m = machines.get(id);

			Future<ResourcesUsage> future = executor
					.submit(new Callable<ResourcesUsage>() {

						@Override
						public ResourcesUsage call() throws Exception {
							return new RetrieveMetrics("http://" + m.getIp()
									+ ":" + m.getTomCatPort()
									+ "/VmMonitor/Metrics")
									.getAverageResoucesUsage();
						}
					});

			futureList.add(future);

		}

		try {
			int i = 0;

			for (long id : machinesId) {

				ResourcesUsage r = futureList.get(i++).get();
				if (r != null)
					metrics.put(id, r);

			}

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return metrics;
	}

}
