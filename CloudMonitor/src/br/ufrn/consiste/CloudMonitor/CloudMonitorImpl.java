package br.ufrn.consiste.CloudMonitor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.ant.DeployTask;

import br.ufrn.consiste.CloudMonitor.DAO.ClientDaoInterface;
import br.ufrn.consiste.CloudMonitor.DAO.ClientDaoJdbc;
import br.ufrn.consiste.CloudMonitor.DAO.MachineDaoInterface;
import br.ufrn.consiste.CloudMonitor.DAO.MachineDaoJdbc;
import br.ufrn.consiste.CloudMonitor.DAO.ResourceUsageDaoInterface;
import br.ufrn.consiste.CloudMonitor.DAO.ResourceUsageDaoJdbc;
import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;
import br.ufrn.consiste.CloudMonitor.Exceptions.DataValidationException;
import br.ufrn.consiste.CloudMonitor.Validation.DataValidation;
import br.ufrn.consiste.CloudMonitor.monitoring.MonitoreVMs;
import br.ufrn.consiste.model.Client;
import br.ufrn.consiste.model.Machine;
import br.ufrn.consiste.model.ResourcesUsage;
import br.ufrn.consiste.model.Thresholds;

public class CloudMonitorImpl extends UnicastRemoteObject implements
		CloudMonitor {

	private static ClientDaoInterface clientDao2 = new ClientDaoJdbc();
	private static ResourceUsageDaoInterface resourceUsageDao2 = new ResourceUsageDaoJdbc();
	private MachineDaoInterface machineDao = new MachineDaoJdbc();
	private DataValidation validation = new DataValidation();

	protected CloudMonitorImpl() throws RemoteException {
		super();

		new MonitoreVMs().start();
	}

	@Override
	public long register(String rmiURL) throws RemoteException {

		try {

			validation.checkEmptyStgring(rmiURL);

			Client c = new Client();
			c.setRmiURL(rmiURL);
			c = clientDao2.save(c);

			return c.getId();

		} catch (DAOException | DataValidationException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public void unregister(Long clientId) throws RemoteException {

		try {

			validation.checkClient(clientId);

			Client c = clientDao2.findById(clientId);

			clientDao2.delete(c);

		} catch (DAOException | DataValidationException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public long monitoreVM(Long clientID, String ipVm, int tomcatPort,
			String tomcatUser, String tomcatPassword, Thresholds thresholds)
			throws RemoteException {

		Client c;
		try {

			validation.checkClient(clientID);

			validation.checkEmptyStgring(ipVm);

			validation.checkNumberPort(tomcatPort);

			validation.checkThresholds(thresholds);

			validation.checkIp(ipVm);

			c = clientDao2.findById(clientID);

			deploymentVmMonitor(ipVm, tomcatPort, tomcatUser, tomcatPassword);

			Machine m = new Machine();

			m.setIp(ipVm);
			m.setTomCatPort(tomcatPort);
			m.setThresholds(thresholds);
			m.setTomcatUser(tomcatUser);
			m.setClientId(clientID);

			m = machineDao.save(m);

			return m.getId();
		} catch (DAOException | DataValidationException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("Erro inesperado: " + e.getMessage());
		}

	}

	@Override
	public void monitoringVMCancel(Long clientId, Long vmId)
			throws RemoteException {

		try {
			
			validation.checkClient(clientId);
			
			validation.checkMachine(vmId);


			Machine m = machineDao.findById(vmId);


			if (m.getClientId() != clientId) {
				throw new RemoteException("A maquina " + vmId
						+ " não pertence ao cliente " + clientId);
			}

			machineDao.delete(m);

		} catch (DAOException | DataValidationException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("Erro inesperado: " + e.getMessage());
		}
	}

	private void deploymentVmMonitor(String ip, int port, String tomcatUser,
			String tomcatPassword) {

		try {
			DeployTask task = new DeployTask();
			task.setUrl("http://" + ip + ":" + port + "/manager/text");
			task.setUsername(tomcatUser);
			task.setPassword(tomcatPassword);
			task.setPath("/VmMonitor");
			task.setWar(new File("VmMonitor.war").getAbsolutePath());

			task.execute();

		} catch (Exception e) {
			System.out.print("Erro ao implantar VmMonitor: " + e.getMessage());
		}

	}

	
	@Override
	public Map<Long, ResourcesUsage> getMestricsVMs(Long clientId, Long[] VMsId)
			throws RemoteException {

		Map<Long, ResourcesUsage> metrics = new HashMap<Long, ResourcesUsage>();

		Client c;
		try {
			validation.checkClient(clientId);
			

			for (long id : VMsId) {
				
				validation.checkMachine(id);

				Machine m = machineDao.findById(id);

				if (m.getClientId() != clientId) {
					throw new RemoteException("A maquina " + id
							+ " não pertence ao cliente " + clientId);
				}

				Date dtInit = new Date();
				Date dtEnd = new Date();

				dtEnd.setHours(dtEnd.getHours() - 12);

				ResourcesUsage rUsage = resourceUsageDao2
						.getAverageResourceUsage(dtInit, dtEnd, id);

				metrics.put(id, rUsage);

			}

		} catch (DAOException | DataValidationException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("Erro inesperado: " + e.getMessage());
		}

		return metrics;
	}

	@Override
	public void updatThresholds(Long clientId, Long vmId, Thresholds thresholds)
			throws RemoteException {
		
		try {
			validation.checkClient(clientId);
			
			validation.checkClient(clientId);
			
			validation.checkThresholds(thresholds);
			
			Machine m = machineDao.findById(vmId);

			if (m.getClientId() != clientId) {
				throw new RemoteException("A maquina " + vmId
						+ " não pertence ao cliente " + clientId);
			}
			
			m.setThresholds(thresholds);
			
			machineDao.update(m);		
			
		} catch (DataValidationException | DAOException e) {
			throw new RemoteException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("Erro inesperado: " + e.getMessage());
		}

		
	}

}
