package br.ufrn.consiste.CloudMonitor.DAO;

import java.util.Date;

import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;
import br.ufrn.consiste.model.ResourcesUsage;

public interface ResourceUsageDaoInterface {

	ResourcesUsage getAverageResourceUsage(Date dtInit, Date dtEnd,long vmId) throws DAOException;
	
	ResourcesUsage save(ResourcesUsage resourceUsage) throws DAOException ;
}
