package br.ufrn.consiste.CloudMonitor.DAO;

import java.util.Date;

import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;
import br.ufrn.consiste.model.ResourcesUsage;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Query;

public class ResourceUsageDao implements ResourceUsageDaoInterface {

	private ObjectContainer db;
	
	public static final ResourceUsageDao resourceUsageDao = new ResourceUsageDao();

	private ResourceUsageDao() {
		
	}
	
	public static ResourceUsageDao getInstance(){
		return resourceUsageDao;
	}
	
	public void openDB(){
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
				"ResourceUsageDatabase.db4o");
	}

	@Override
	public synchronized ResourcesUsage save(ResourcesUsage entity) throws DAOException {
		try {
			openDB();
			
			db.store(entity);

			return entity;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		} finally {
			db.close();
		}
	}

	private ResourcesUsage calculateAverage(
			ObjectSet<ResourcesUsage> resourcesUsages) {

		ResourcesUsage reUsage = null;

		double cpuUsage = 0, memoryUsage = 0, storageUsage = 0;
		long txBytes = 0, rxBytes = 0;

		while (resourcesUsages.hasNext()) {

			ResourcesUsage r = resourcesUsages.next();

			cpuUsage += r.getCpuUsage();
			memoryUsage += r.getMemoryUsage();
			storageUsage += r.getStorageUsage();
			rxBytes += r.getRxBytes();
			txBytes += r.getTxBytes();

		}

		if (resourcesUsages.size() > 0) {

			reUsage = new ResourcesUsage();

			reUsage.setCpuUsage(cpuUsage / resourcesUsages.size());
			reUsage.setMemoryUsage(memoryUsage / resourcesUsages.size());
			reUsage.setRxBytes(rxBytes / resourcesUsages.size());
			reUsage.setStorageUsage(storageUsage / resourcesUsages.size());
			reUsage.setTxBytes(txBytes / resourcesUsages.size());
		}

		return reUsage;

	}

	@Override
	public synchronized ResourcesUsage getAverageResourceUsage(Date dtInit, Date dtEnd,
			long vmId) throws DAOException {

		try {
			
			openDB();
			
			Query query = db.query();

			ResourcesUsage resourcesUsage = new ResourcesUsage();
			resourcesUsage.setMachineId(vmId);

			query.constrain(resourcesUsage);
			// query.descend("date").orderAscending();

			Constraint c1 = query.descend("date").constrain(dtInit).greater();
			Constraint c2 = query.descend("date").constrain(dtEnd).smaller();
			c1.and(c2);

			ObjectSet<ResourcesUsage> result = query.execute();

			return calculateAverage(result);
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		} finally {
			db.close();
		}
	}

}
