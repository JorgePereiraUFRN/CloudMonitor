package br.ufrn.consiste.CloudMonitor.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;
import br.ufrn.consiste.model.ResourcesUsage;

public class ResourceUsageDaoJdbc implements ResourceUsageDaoInterface {

	private PreparedStatement pstm;
	private ResultSet rs;
	private final acessBD bd = new acessBD();

	@Override
	public ResourcesUsage getAverageResourceUsage(Date dtInit, Date dtEnd,
			long vmId) throws DAOException {

		String sql = "select AVG(cpuUsage)cpuUsage, AVG(memoryUsage) memoryUsage,"
				+ " AVG(storageUsage) storageUsage, AVG(txUsage) txUsage, AVG(rxUsage)"
				+ " rxUsage from resourceUsage where machineId = ? and"
				+ " date BETWEEN ? and ?";

		try {
			pstm = bd.conect().prepareStatement(sql);
			
			pstm.setLong(1, vmId);
			pstm.setDate(2, new java.sql.Date(dtInit.getTime()));
			pstm.setDate(3, new java.sql.Date(dtEnd.getTime()));

			rs = pstm.executeQuery();
			ResourcesUsage r = null;

			if (rs.next()) {
				
				r = new ResourcesUsage();
				
				r.setCpuUsage(rs.getDouble("cpuUsage"));
				r.setMemoryUsage(rs.getDouble("memoryUsage"));
				r.setRxBytes(rs.getLong("rxUsage"));
				r.setStorageUsage(rs.getDouble("storageUsage"));
				r.setTxBytes(rs.getLong("txUsage"));
				
				r.setMachineId(vmId);
				r.setDate(dtEnd);
				
			}

			return r;
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}

	@Override
	public ResourcesUsage save(ResourcesUsage resourceUsage)
			throws DAOException {
		String sql = "Insert into resourceUsage (cpuUsage, memoryUsage, storageUsage, txUsage, "
				+ " rxUsage, date, machineId) values (?, ?, ?, ?, ?, ?, ?)";
		try {
			pstm = bd.conect().prepareStatement(sql);

			pstm.setDouble(1, resourceUsage.getCpuUsage());
			pstm.setDouble(2, resourceUsage.getMemoryUsage());
			pstm.setDouble(3, resourceUsage.getStorageUsage());
			pstm.setLong(4, resourceUsage.getTxBytes());
			pstm.setLong(5, resourceUsage.getRxBytes());
			pstm.setDate(6, new java.sql.Date(resourceUsage.getDate().getTime()));
			pstm.setLong(7, resourceUsage.getMachineId());

			pstm.execute();
			bd.disconect();

			return resourceUsage;

		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}

}
