package br.ufrn.consiste.CloudMonitor.Validation;

import javax.xml.bind.ValidationException;

import br.ufrn.consiste.CloudMonitor.ClientCloudMonitor;
import br.ufrn.consiste.CloudMonitor.DAO.ClientDaoInterface;
import br.ufrn.consiste.CloudMonitor.DAO.ClientDaoJdbc;
import br.ufrn.consiste.CloudMonitor.DAO.MachineDaoInterface;
import br.ufrn.consiste.CloudMonitor.DAO.MachineDaoJdbc;
import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;
import br.ufrn.consiste.CloudMonitor.Exceptions.DataValidationException;
import br.ufrn.consiste.model.Client;
import br.ufrn.consiste.model.Machine;
import br.ufrn.consiste.model.Thresholds;

public class DataValidation {

	private static ClientDaoInterface clientDao = new ClientDaoJdbc();
	private MachineDaoInterface machineDao = new MachineDaoJdbc();

	public DataValidation() {
		// TODO Auto-generated constructor stub
	}

	public boolean checkEmptyStgring(String str) throws DataValidationException {

		if (str == null || str.equals("")) {
			throw new DataValidationException("Valor inválido (vazio)");
		}

		return true;
	}

	public boolean checkClient(Long clientId) throws DataValidationException,
			DAOException {

		if (clientId < 0) {
			throw new DataValidationException(
					"O identificadcor do cliente é inválido.");
		}
		Client c;
		c = clientDao.findById(clientId);
		if (c == null) {
			throw new DataValidationException("Não existe cliente com id "
					+ clientId + " registrado.");
		}

		return true;

	}

	public boolean checkNumberPort(int port) throws DataValidationException {

		if (port < 0 || port > 65536) {
			throw new DataValidationException("Porta inválida: " + port);
		}

		return true;
	}

	public boolean checkThresholds(Thresholds t) throws DataValidationException {

		if (t.getCpuMaxThreshold() > 1) {
			throw new DataValidationException("CpuMaxThreshold inválido.");
		}
		/*
		 * if(t.getCpuMinThreshold() < 0){ throw new
		 * DataValidationException("CpuMinThreshold inválido."); }
		 */
		if (t.getMemoryMaxThreshold() > 1) {
			throw new DataValidationException("MemoryMaxThreshold inválido.");
		}
		/*
		 * if(t.getMemoryMinThreshold() < 0){ throw new
		 * DataValidationException("MemoryMinThreshold inválido."); }
		 */
		if (t.getStorageMaxThreshold() > 1) {
			throw new DataValidationException("StorageMaxThreshold inválido.");
		}
		/*
		 * if(t.getStorageMinThreshold() < 0){ throw new
		 * DataValidationException("MemoryMinThreshold inválido."); }
		 */

		return true;
	}

	public boolean checkIp(String ip) throws DAOException,
			DataValidationException {

		Machine m = machineDao.findMachineByIp(ip);

		if (m != null) {
			throw new DataValidationException(
					"Já existe uma máquina cadastrada com o ip " + ip);
		}

		return true;
	}

	public boolean checkMachine(Long machineId) throws DataValidationException, DAOException {
		Machine m = machineDao.findById(machineId);

		if (m == null) {
			throw new DataValidationException(
					"Não existe uma máquina cadastrada com o id " + machineId);
		}

		return true;
	}

}
