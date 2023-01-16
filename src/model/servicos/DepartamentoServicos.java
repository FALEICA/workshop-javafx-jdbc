package model.servicos;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartamentoServicos {

	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> buscaTodos(){
		return dao.findAll();
		
		
	}
}
