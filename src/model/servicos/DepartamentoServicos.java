package model.servicos;

import java.util.ArrayList;
import java.util.List;

import modelo.entidades.Departamento;

public class DepartamentoServicos {

	public List<Departamento> buscaTodos(){
		List<Departamento> list = new ArrayList<>();
		list.add(new Departamento(1, "Livros"));
		list.add(new Departamento(2, "Computadores"));
		list.add(new Departamento(3, "Eletronicos"));
		list.add(new Departamento(4, "Esportivos"));
		
		return list;
		
	}
}
