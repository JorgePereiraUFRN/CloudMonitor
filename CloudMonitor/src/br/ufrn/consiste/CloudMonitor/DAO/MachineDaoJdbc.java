package br.ufrn.consiste.CloudMonitor.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.MaskFormatter;

import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;
import br.ufrn.consiste.model.Client;
import br.ufrn.consiste.model.Machine;
import br.ufrn.consiste.model.Thresholds;

public class MachineDaoJdbc implements MachineDaoInterface {

	private PreparedStatement pstm;
	private ResultSet rs;
	private final acessBD bd = new acessBD();

	public MachineDaoJdbc() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Machine findById(Long id) throws DAOException {

		String sql = "Select * from machine where id = ?";

		try {
			pstm = bd.conect().prepareStatement(sql);

			pstm.setLong(1, id);

			rs = pstm.executeQuery();

			Machine m = null;

			List<Machine> machines = getMachines(rs);

			bd.disconect();

			if (machines != null && machines.size() > 0) {
				m = machines.get(0);
			}

			return m;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}
	}

	private List<Machine> getMachines(ResultSet rs) throws SQLException {

		Thresholds t;
		List<Machine> machines = new ArrayList<Machine>();
		Machine m = null;

		if (rs.next()) {
			m = new Machine();

			m.setId(rs.getLong("id"));
			m.setTomCatPort(rs.getInt("tomCatPort"));
			m.setIp(rs.getString("ip"));
			m.setTomcatUser(rs.getString("tomcatUser"));
			m.setClientId(rs.getLong("clientId"));

			t = new Thresholds();

			t.setCpuMaxThreshold(rs.getDouble("maxCpu"));
			t.setCpuMinThreshold(rs.getDouble("minCpu"));
			t.setStorageMaxThreshold(rs.getDouble("maxStorage"));
			t.setStorageMinThreshold(rs.getDouble("minStorage"));
			t.setMemoryMaxThreshold(rs.getDouble("maxMemory"));
			t.setMemoryMinThreshold(rs.getDouble("minMemory"));
			t.setTxBytesMaxThreshold(rs.getLong("maxTx"));
			t.setTxBytesMinThreshold(rs.getLong("minTx"));
			t.setRxBytesMaxThreshold(rs.getLong("maxRx"));
			t.setRxBytesMinThreshold(rs.getLong("minRx"));

			m.setThresholds(t);

			machines.add(m);
		}
		return machines;
	}

	@Override
	public List<Machine> findAll() throws DAOException {

		String sql = "Select * from machine";

		try {
			pstm = bd.conect().prepareStatement(sql);

			rs = pstm.executeQuery();

			List<Machine> machines = new ArrayList<Machine>();

			machines = getMachines(rs);

			bd.disconect();

			return machines;

		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}

	@Override
	public Machine save(Machine entity) throws DAOException {

		String sql = "Insert into machine (tomCatPort, ip, tomCatUser, clientId, "
				+ "maxCpu, minCpu, maxStorage, minStorage, maxMemory, minMemory, "
				+ "maxTx, minTx,  maxRx, minRx) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstm = bd.conect().prepareStatement(sql);

			pstm.setInt(1, entity.getTomCatPort());
			pstm.setString(2, entity.getIp());
			pstm.setString(3, entity.getTomcatUser());
			pstm.setLong(4, entity.getClientId());
			pstm.setDouble(5, entity.getThresholds().getCpuMaxThreshold());
			pstm.setDouble(6, entity.getThresholds().getCpuMinThreshold());
			pstm.setDouble(7, entity.getThresholds().getStorageMaxThreshold());
			pstm.setDouble(8, entity.getThresholds().getStorageMinThreshold());
			pstm.setDouble(9, entity.getThresholds().getMemoryMaxThreshold());
			pstm.setDouble(10, entity.getThresholds().getMemoryMinThreshold());
			pstm.setLong(11, entity.getThresholds().getTxBytesMaxThreshold());
			pstm.setLong(12, entity.getThresholds().getTxBytesMinThreshold());
			pstm.setLong(13, entity.getThresholds().getRxBytesMaxThreshold());
			pstm.setLong(14, entity.getThresholds().getRxBytesMinThreshold());

			pstm.execute();
			bd.disconect();

			pstm = bd.conect().prepareStatement(
					"select MAX(id) id from machine");
			rs = pstm.executeQuery();

			if (rs.next()) {
				entity.setId(rs.getLong("id"));
			}

			bd.disconect();

			return entity;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}

	}

	@Override
	public Machine update(Machine entity) throws DAOException {
		String sql = "update machine set tomCatPort = ?, ip = ?, tomCatUser = ?, clientId = ?, "
				+ "maxCpu = ?, minCpu = ?, maxStorage = ?, minStorage = ?, maxMemory = ?, minMemory = ?, "
				+ "maxTx = ?, minTx = ?,  maxRx = ?, minRx = ? where id = ?";
		try {
			pstm = bd.conect().prepareStatement(sql);

			pstm.setInt(1, entity.getTomCatPort());
			pstm.setString(2, entity.getIp());
			pstm.setString(3, entity.getTomcatUser());
			pstm.setLong(4, entity.getClientId());
			pstm.setDouble(5, entity.getThresholds().getCpuMaxThreshold());
			pstm.setDouble(6, entity.getThresholds().getCpuMinThreshold());
			pstm.setDouble(7, entity.getThresholds().getStorageMaxThreshold());
			pstm.setDouble(8, entity.getThresholds().getStorageMinThreshold());
			pstm.setDouble(9, entity.getThresholds().getMemoryMaxThreshold());
			pstm.setDouble(10, entity.getThresholds().getMemoryMinThreshold());
			pstm.setLong(11, entity.getThresholds().getTxBytesMaxThreshold());
			pstm.setLong(12, entity.getThresholds().getTxBytesMinThreshold());
			pstm.setLong(13, entity.getThresholds().getRxBytesMaxThreshold());
			pstm.setLong(14, entity.getThresholds().getRxBytesMinThreshold());
			pstm.setLong(15, entity.getId());

			pstm.execute();
			bd.disconect();

			return entity;

		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}
	}

	@Override
	public void delete(Machine entity) throws DAOException {
		String sql = "Delete from machine where id = ?";
		try {
			pstm = (PreparedStatement) bd.conect().prepareStatement(sql);
			pstm.setLong(1, entity.getId());
			pstm.execute();
			bd.disconect();
		} catch (SQLException e) {
			throw new DAOException(e.getMessage());
		}

	}

	@Override
	public void upadateThresholds(Thresholds thresholds, long machineId)
			throws DAOException {

		Machine m = findById(machineId);

		if (m != null) {
			m.setThresholds(thresholds);

			update(m);
		} else {
			throw new DAOException("a máquina com id: " + machineId
					+ " não existe.");

		}

	}

	@Override
	public Machine findMachineByIp(String ip) throws DAOException {
		String sql = "Select * from machine where ip = ?";

		try {
			pstm = bd.conect().prepareStatement(sql);

			pstm.setString(1, ip);

			rs = pstm.executeQuery();

			Machine m = null;

			List<Machine> machines = getMachines(rs);

			bd.disconect();

			if (machines != null && machines.size() > 0) {
				m = machines.get(0);
			}

			return m;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}
	}
	
	public static void main(String args[]) throws DAOException{
		
		MachineDaoJdbc dao = new MachineDaoJdbc();
		
		Machine m = dao.findById(10L);
		
		if(m != null){
			System.out.println(m);
		}
		
	}

}
