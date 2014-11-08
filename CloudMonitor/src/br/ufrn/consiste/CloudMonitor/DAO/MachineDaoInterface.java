package br.ufrn.consiste.CloudMonitor.DAO;

import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;
import br.ufrn.consiste.model.Machine;
import br.ufrn.consiste.model.Thresholds;

public interface MachineDaoInterface extends GenericDaoInterface<Machine, Long>{
	
	void upadateThresholds(Thresholds thresholds, long machineId) throws DAOException;
	
	Machine findMachineByIp(String ip) throws DAOException;

}
 