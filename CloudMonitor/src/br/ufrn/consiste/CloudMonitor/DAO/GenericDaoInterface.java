package br.ufrn.consiste.CloudMonitor.DAO;

import java.io.Serializable;
import java.util.List;

import br.ufrn.consiste.CloudMonitor.Exceptions.DAOException;

public interface GenericDaoInterface<T, ID extends Serializable> {
	
	public T findById(ID id) throws DAOException;
	 
	public List<T> findAll()throws DAOException;
 
	public T save(T entity)throws DAOException;
 
	public T update(T entity)throws DAOException;
        
	public void delete(T entity)throws DAOException;
}
